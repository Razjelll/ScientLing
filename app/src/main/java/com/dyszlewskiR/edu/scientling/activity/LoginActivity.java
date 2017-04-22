package com.dyszlewskiR.edu.scientling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.net.requests.LoginRequest;
import com.dyszlewskiR.edu.scientling.net.responses.LoginResponse;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.utils.MD5;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private TextView mErrorTextView;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mNotRememberTextView;
    private TextView mRegisterTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupControls();
        setListeners();
    }

    private void setupControls() {
        mLoginEditText = (EditText) findViewById(R.id.login_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mNotRememberTextView = (TextView) findViewById(R.id.not_remember_button);
        mRegisterTextView = (TextView) findViewById(R.id.register_button);
    }

    private void setListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTask task = new LoginAsyncTask();
                LoginParams params = null;
                try {
                    params = new LoginParams(mLoginEditText.getText().toString(), MD5.getMD5(mPasswordEditText.getText().toString()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                task.execute(params);
            }
        });

        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistrationActivity();
            }
        });
    }

    private void startRegistrationActivity() {
        Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
        startActivity(intent);
    }

    private class LoginParams {
        private String mUsername;
        private String mPassword;

        public LoginParams(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        public String getUsername() {
            return mUsername;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    private final String LOGIN = "login";

    private class LoginAsyncTask extends AsyncTask<LoginParams, Void, LoginResponse.Params> {

        @Override
        protected LoginResponse.Params doInBackground(LoginParams... params) {
            LoginRequest request = new LoginRequest(params[0].getUsername(), params[0].getPassword());
            try {
                LoginResponse response = new LoginResponse(request.start());
                LoginResponse.Params responseParams = response.getParams();
                response.closeConnection();
                return responseParams;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(LoginResponse.Params result) {
            if(result != null && result.getResponseCode()==LoginResponse.LOGIN_SUCCESS){
                LogPref.setLogged(true, getBaseContext());
                LogPref.setLogin(result.getLogin(), getBaseContext());
                Intent intent = new Intent();
                intent.putExtra("login", result.getLogin());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                if (mErrorTextView == null) {
                    mErrorTextView = (TextView) findViewById(R.id.error_text_view);
                }
                mErrorTextView.setText(getString(R.string.bad_login));
                mErrorTextView.setVisibility(View.VISIBLE);
            }
        }
    }


}
