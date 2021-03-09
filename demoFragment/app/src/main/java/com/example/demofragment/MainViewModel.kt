package com.example.demofragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val age = MutableLiveData<String>()

    fun sendDate(nameStr: String, ageStr: String) {
        name.postValue(nameStr)
        age.postValue(ageStr)
    }
}