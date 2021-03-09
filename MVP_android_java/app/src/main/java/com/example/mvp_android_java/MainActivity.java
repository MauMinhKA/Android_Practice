package com.example.mvp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoginInterface {
    private EditText edt_Email, edt_Password;
    private TextView tv_Message;
    private Button btn_Submit;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_Email =findViewById(R.id.edt_email);
        edt_Password =findViewById(R.id.edt_password);
        tv_Message =findViewById(R.id.tv_message);
        btn_Submit =findViewById(R.id.btn_submit);
        loginPresenter = new LoginPresenter(this);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSubmit();
            }
        });
    }

    private void clickSubmit() {
        String email = edt_Email.getText().toString().trim();
        String password = edt_Password.getText().toString().trim();
        User user =new User(email, password);
        loginPresenter.Login(user);

    }

    @Override
    public void loginSuccess() {
        tv_Message.setVisibility(View.VISIBLE);
        tv_Message.setText("login successful");
        tv_Message.setTextColor(getResources().getColor(R.color.design_default_color_primary));

    }

    @Override
    public void loginError() {
        tv_Message.setVisibility(View.VISIBLE);
        tv_Message.setText("email or password is not valid");
        tv_Message.setTextColor(getResources().getColor(R.color.design_default_color_error));

    }
}