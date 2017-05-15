package com.dyszlewskiR.edu.scientling.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dyszlewskiR.edu.scientling.R;
import com.dyszlewskiR.edu.scientling.services.net.requests.RegisterRequest;
import com.dyszlewskiR.edu.scientling.services.net.responses.RegisterResponse;
import com.dyszlewskiR.edu.scientling.utils.MD5;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private final int PASSWORD_LENGTH = 5;
    private final String[] INCORRECT_ELEMENTS = {"-", "_", "=",};

    private EditText mLoginEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mRepeatPassEditText;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupToolbar();
        setupControls();
        setListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.registration));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupControls() {
        mLoginEditText = (EditText) findViewById(R.id.login_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mRepeatPassEditText = (EditText) findViewById(R.id.repeat_password_edit_text);
        mRegisterButton = (Button) findViewById(R.id.register_button);
    }

    private void setListeners() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    RegisterAsyncTask task = new RegisterAsyncTask();
                    task.execute(new RegisterParams(mLoginEditText.getText().toString(),
                            mEmailEditText.getText().toString(),
                            mPasswordEditText.getText().toString()));
                }
            }
        });
    }

    private boolean validate() {
        boolean correct = true;
        if (mLoginEditText.getText().toString().isEmpty()) {
            mLoginEditText.setError(getString(R.string.not_empty_field));
            correct = false;
        }
        if (!checkEmail(mEmailEditText.getText().toString())) {
            correct = false;
        }
        if (!checkPasswords(mPasswordEditText.getText().toString(), mRepeatPassEditText.getText().toString())) {
            correct = false;
        }
        return correct;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean checkEmail(String email) {
        if (email.isEmpty()) {
            mEmailEditText.setError(getString(R.string.not_empty_field));
            return false;
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            mEmailEditText.setError(getString(R.string.email_incorrect));
            return false;
        }
        return true;
    }

    public boolean checkPasswords(String password, String repeatPassword) {
        if (password.isEmpty()) {
            mPasswordEditText.setError(getString(R.string.not_empty_field));
            return false;
        }
        if (password.length() < PASSWORD_LENGTH) {
            mPasswordEditText.setError(getString(R.string.short_password) + " " + PASSWORD_LENGTH + " " + getString(R.string.signs));
            return false;
        }

        /*if (!password.matches(".*\\d+.*")) {
            mPasswordEditText.setError(getString(R.string.must_contain_number));
            return false;
        }*/

        for (String element : INCORRECT_ELEMENTS) {
            if (password.contains(element)) {
                mPasswordEditText.setError(getString(R.string.not_contain_element) + getIncorrectElementList());
                return false;
            }
        }

        if (!password.equals(repeatPassword)) {
            mPasswordEditText.setError(getString(R.string.password_different));
            return false;
        }

        return true;
    }

    private String getIncorrectElementList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < INCORRECT_ELEMENTS.length; i++) {
            stringBuilder.append(INCORRECT_ELEMENTS[i]);
            if (i != INCORRECT_ELEMENTS.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RegisterParams {
        private String mLogin;
        private String mEmail;
        private String mPassword;

        public RegisterParams(String login, String email, String password) {
            mLogin = login;
            mEmail = email;
            mPassword = password;
        }

        public String getLogin() {
            return mLogin;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    private class RegisterAsyncTask extends AsyncTask<RegisterParams, Void, Integer> {

        @Override
        protected Integer doInBackground(RegisterParams... params) {
            try {
                String login = params[0].getLogin();
                String email = params[0].getEmail();
                String password = MD5.getMD5(params[0].getPassword());
                RegisterRequest registerRequest = new RegisterRequest();
                RegisterResponse response = new RegisterResponse(registerRequest.start(login, email, password));
                int responseCode = response.getResultCode();
                response.closeConnection();
                return responseCode;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result != null) {
                switch (result) {
                    case RegisterResponse.OK:
                        Toast.makeText(getBaseContext(), getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case RegisterResponse.EXIST_EMAIL:
                        mEmailEditText.setError(getString(R.string.email_exist));
                        mLoginEditText.setError(null);
                        break;
                    case RegisterResponse.EXIST_LOGIN:
                        mLoginEditText.setError(getString(R.string.login_exist));
                        mEmailEditText.setError(null);
                        break;
                    case RegisterResponse.EXIST_BOTH:
                        mEmailEditText.setError(getString(R.string.email_exist));
                        mLoginEditText.setError(getString(R.string.login_exist));
                        break;
                }
            }
        }
    }
}
