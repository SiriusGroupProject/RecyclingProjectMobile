package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button signUpButton;
    private EditText nameText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bindViews();
    }

    private void bindViews() {
        nameText = (EditText) findViewById(R.id.name);
        passwordText = (EditText) findViewById(R.id.password);
        signUpButton = (Button) findViewById(R.id.loginButton);
    }
}
