package com.example.mvp_android_java;

public class LoginPresenter {

    private LoginInterface loginInterface;

    public LoginPresenter(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }

    public void Login(User user){
        if (user.isValidEmail() && user.isPassword()){
            loginInterface.loginSuccess();
        }else {
            loginInterface.loginError();
        }
    }
}
