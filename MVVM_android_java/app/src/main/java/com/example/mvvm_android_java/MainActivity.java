package com.example.mvvm_android_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.mvvm_android_java.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final int nameDemo = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            LoginViewModel loginViewModel = new LoginViewModel();
            activityMainBinding.setLoginViewModel(loginViewModel);
    }
}