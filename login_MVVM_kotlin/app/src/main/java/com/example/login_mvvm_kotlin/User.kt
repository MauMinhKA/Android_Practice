package com.example.login_mvvm_kotlin



import android.text.TextUtils
import android.util.Patterns

class User(private var email: String, private var password: String) {

    fun isValidEmail(): Boolean {
        return !TextUtils.isEmpty(email)  && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    fun isPassword(): Boolean{
        return !TextUtils.isEmpty(password) && password.length >= 8
    }
}