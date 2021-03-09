package com.example.mvc_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText edt_Email, edt_Password;
    private TextView tv_Message;
    private Button btn_Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_Email =findViewById(R.id.edt_email);
        edt_Password =findViewById(R.id.edt_password);
        tv_Message =findViewById(R.id.tv_message);
        btn_Submit =findViewById(R.id.btn_submit);

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

        tv_Message.setVisibility(View.VISIBLE);
        if (user.isValidEmail() && user.isPassword()){
            tv_Message.setText("login successful");
            tv_Message.setTextColor(getResources().getColor(R.color.design_default_color_primary));
        }else {
            tv_Message.setText("email or password is not valid");
            tv_Message.setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
        }

    }
}