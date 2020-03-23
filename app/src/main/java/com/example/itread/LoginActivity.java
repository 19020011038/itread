package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout login_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        login_register = findViewById(R.id.login_register);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
