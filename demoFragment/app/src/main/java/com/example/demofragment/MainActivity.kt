package com.example.demofragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity(), CommunicationInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    override fun onClickTopFragment(name: String, age: String) {
        val fragment2 = supportFragmentManager.findFragmentByTag(MyFragment2.TAG) as MyFragment2
        fragment2.showInfo(name, age)
    }
}