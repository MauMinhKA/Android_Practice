package com.example.recyclerview_mvvm_livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel() : ViewModel() {
    internal var mListUserLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private lateinit var mListUser: List<User>

    init {
        initData()
    }

    private fun initData() {
        mListUser = ArrayList()
        (mListUser as ArrayList<User>).add(User(R.drawable.img1, "MauMinh", "MauMinh Description"))
        mListUserLiveData.value = mListUser

    }

    fun addUser(user: User) {
        (mListUser as ArrayList<User>).add(user)
        mListUserLiveData.value = mListUser
    }


}

