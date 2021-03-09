package com.example.login_mvvm_kotlin


import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField


class LoginViewModel {
    var email: String = ""
    var password: String = ""
    val messageLogin = ObservableField<String>()
    var isShowMess = ObservableField<Boolean>()
    var colorSuccess = ObservableField<Boolean>()


    fun onClickLogin() {
        isShowMess.set(true)
        val user = User(email, password)
        if (user.isValidEmail() && user.isPassword()) {
            messageLogin.set("login successful")
            colorSuccess.set(true)
        } else {
            messageLogin.set("email or password is valid")
            colorSuccess.set(false)
        }
    }
}