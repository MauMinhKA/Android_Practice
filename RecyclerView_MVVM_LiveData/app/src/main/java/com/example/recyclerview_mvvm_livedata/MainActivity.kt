package com.example.recyclerview_mvvm_livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    private var index: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        rcv_user.layoutManager = linearLayoutManager;

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.mListUserLiveData.observe(this, Observer {
            userAdapter = UserAdapter(it)
            rcv_user.adapter = userAdapter

        })
        btn_submit.setOnClickListener(View.OnClickListener {
            clickAddUser()
            Toast.makeText(this,"this is toast message", Toast.LENGTH_SHORT).show()
        })
    }

    private fun clickAddUser() {
        val user = User(R.drawable.img1, "MauMinh$index", "MauMinh desciption$index")
        userViewModel.addUser(user)
        index++
    }


}