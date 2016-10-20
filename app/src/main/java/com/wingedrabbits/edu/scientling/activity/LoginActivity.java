package com.wingedrabbits.edu.scientling.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.wingedrabbits.edu.scientling.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginField;
    private EditText passwordField;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prepareComponents();
    }

    private void prepareComponents()
    {
        loginField = (EditText)findViewById(R.id.loginEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);
        loginButton = (Button)findViewById(R.id.loginButton);

        String loginFieldHint = getString(R.string.login_hint);
        loginField.setHint(loginFieldHint);
        String passwordFieldHint = getString(R.string.password_hint);
        passwordField.setHint(passwordFieldHint);
        String loginButtonText = getString(R.string.log_in);
        loginButton.setText(loginButtonText);
    }
}
