package com.dyszlewskiR.edu.scientling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.preferences.LogPref;
import com.dyszlewskiR.edu.scientling.services.net.requests.LoginRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.LoginResponse;
import com.dyszlewskiR.edu.scientling.services.net.utils.ConnectivityUtils;
import com.dyszlewskiR.edu.scientling.utils.MD5;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private TextView mErrorTextView;
    private TextView mInfoTextView;
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
        mInfoTextView = (TextView) findViewById(R.id.info_text_view);
        mErrorTextView = (TextView) findViewById(R.id.error_text_view);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mNotRememberTextView = (TextView) findViewById(R.id.not_remember_button);
        mRegisterTextView = (TextView) findViewById(R.id.register_button);
    }

    private void setListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityUtils.isConnected(getBaseContext())) {
                    setLoggingControls(true, false, getString(R.string.wait_logging));
                    startLoginAsyncTask();
                } else {
                    setLoggingControls(false, true, getString(R.string.log_no_net));
                }
            }
        });

        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistrationActivity();
            }
        });
    }

    private void setLoggingControls(boolean isLogging, boolean isError, String infoTextView) {
        if (isLogging) {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText(infoTextView);
            mErrorTextView.setVisibility(View.GONE);
            mLoginButton.setEnabled(false);
        } else if (isError) {
            mInfoTextView.setVisibility(View.GONE);
            mErrorTextView.setVisibility(View.VISIBLE);
            mErrorTextView.setText(infoTextView);
            mLoginButton.setEnabled(true);
        } else {
            mInfoTextView.setVisibility(View.GONE);
            mErrorTextView.setVisibility(View.GONE);
            mLoginButton.setEnabled(true);
        }

    }

    private void startLoginAsyncTask() {
        LoginAsyncTask task = new LoginAsyncTask(new LoginCallback() {
            @Override
            public void onSuccessLogin(String login) {
                Intent intent = new Intent();
                intent.putExtra("login", login);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onIncorrectData() {
                setLoggingControls(false, true, getString(R.string.bad_login));

            }

            @Override
            public void onError() {
                setLoggingControls(false, true, getString(R.string.logging_error));
            }
        });
        LoginParams params = null;
        try {
            params = new LoginParams(mLoginEditText.getText().toString(), MD5.getMD5(mPasswordEditText.getText().toString()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        task.execute(params);
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

        private LoginCallback mCallback;
        private LoginParams mParams;

        public LoginAsyncTask(LoginCallback callback) {
            mCallback = callback;
        }

        @Override
        protected LoginResponse.Params doInBackground(LoginParams... params) {
            mParams = params[0];
            LoginRequest request = new LoginRequest(params[0].getUsername(), params[0].getPassword());
            try {
                LoginResponse response = new LoginResponse(request.start());
                LoginResponse.Params responseParams = response.getParams();
                response.closeConnection();
                return responseParams;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(LoginResponse.Params result) {
            if (result != null) {
                switch (result.getResponseCode()) {
                    case LoginResponse.LOGIN_SUCCESS:
                        login(result.getLogin());
                        break;
                    case LoginResponse.INCORRECT_DATA:
                        mCallback.onIncorrectData();
                        break;
                    case LoginResponse.ERROR:
                        mCallback.onError();
                        break;
                }
            } else {
                mCallback.onError();
            }
        }

        private void login(String login) {
            LogPref.setLogged(true, getBaseContext());
            LogPref.setLogin(login, getBaseContext());
            /*try {
                //LogPref.setPassword(MD5.getMD5(mParams.getPassword()), getBaseContext());
                ;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }*/
            LogPref.setPassword(mParams.getPassword(), getBaseContext());
            if (mCallback != null) {
                mCallback.onSuccessLogin(login);
            }
        }
    }


}

interface LoginCallback {
    void onSuccessLogin(String login);

    void onIncorrectData();

    void onError();
}
