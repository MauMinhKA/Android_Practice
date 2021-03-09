package com.example.mvvm_android_java;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class LoginViewModel extends BaseObservable {

    private String email;
    private String password;
    public ObservableField<String>  messageLogin = new ObservableField<>();
    public ObservableField<Boolean> isShowMess = new ObservableField<>();
    public ObservableField<Boolean> colorSuccess = new ObservableField<>();

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void onClickLogin(){
        isShowMess.set(true);
        User user =new User(getEmail(), getPassword());
        if (user.isValidEmail() && user.isPassword()){
            messageLogin.set("Login successful");
            colorSuccess.set(true);
        }else {
            messageLogin.set("Email or Password is not valid");
            colorSuccess.set(false);
        }
    }
}
