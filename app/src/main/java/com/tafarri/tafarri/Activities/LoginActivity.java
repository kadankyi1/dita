package com.tafarri.tafarri.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;
import com.tafarri.tafarri.Util.MyLifecycleHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mEnterEmailTextView, mEnterCodeTextView;
    private TextInputLayout mEmailEditTextHolder, mPasscodeEditTextHolder;
    private EditText mEmailEditText, mPasscodeEditText;
    private Button mSendCodeButton, mLoginButton;
    private ProgressBar mLoginLoader;
    private Thread loginThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEnterEmailTextView = findViewById(R.id.activity_login_receivecode_textview);
        mEmailEditTextHolder = findViewById(R.id.activity_login_phone_edit_text_layout_holder);
        mEmailEditText = findViewById(R.id.activity_login_phone_edit_text);
        mSendCodeButton = findViewById(R.id.activity_login_login_button);
        mEnterCodeTextView = findViewById(R.id.activity_login_entercode_textview);
        mPasscodeEditTextHolder = findViewById(R.id.activity_login_code_edit_text_layout_holder);
        mPasscodeEditText = findViewById(R.id.activity_login_code_edit_text);
        mLoginButton = findViewById(R.id.activity_login_verify_code_button);
        mLoginLoader = findViewById(R.id.activity_login_loader);

        mSendCodeButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSendCodeButton.getId()){
            if(!mEmailEditText.getText().toString().trim().equalsIgnoreCase("")){
                loginThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendLoginCode(mEmailEditText.getText().toString().trim());
                    }
                });
                loginThread.start();
            }
        } else if(view.getId() == mLoginButton.getId()){
            if(!mPasscodeEditText.getText().toString().trim().equalsIgnoreCase("") && !mEmailEditText.getText().toString().trim().equalsIgnoreCase("")){
                verifyLoginCode(mEmailEditText.getText().toString().trim(), mPasscodeEditText.getText().toString().trim());
            }
        }
    }

    private void sendLoginCode(String emailAddress) {

        Log.e("PSignup emailAddress", emailAddress);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // UI ACTIONS COME HERE
                mEnterEmailTextView.setVisibility(View.VISIBLE);
                mEnterCodeTextView.setVisibility(View.INVISIBLE);
                mEmailEditTextHolder.setVisibility(View.INVISIBLE);
                mEmailEditText.setVisibility(View.INVISIBLE);
                mSendCodeButton.setVisibility(View.INVISIBLE);
                mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                mPasscodeEditText.setVisibility(View.INVISIBLE);
                mLoginButton.setVisibility(View.INVISIBLE);
                mLoginLoader.setVisibility(View.VISIBLE);
            }
        });

        AndroidNetworking.post(Config.LINK_SEND_LOGIN_CODE)
                .addBodyParameter("user_email", emailAddress)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("login_activity_send_verify_code")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.e("PSignup", response);

                if(!LoginActivity.this.isFinishing()){
                    try {
                        JSONObject o = new JSONObject(response);
                        String myStatus = o.getString("status");
                        final String myStatusMessage = o.getString("message");

                        if (myStatus.equalsIgnoreCase("success")) {

                            mEnterEmailTextView.setVisibility(View.INVISIBLE);
                            mEnterCodeTextView.setVisibility(View.VISIBLE);
                            mEmailEditTextHolder.setVisibility(View.INVISIBLE);
                            mEmailEditText.setVisibility(View.INVISIBLE);
                            mSendCodeButton.setVisibility(View.INVISIBLE);
                            mPasscodeEditTextHolder.setVisibility(View.VISIBLE);
                            mPasscodeEditText.setVisibility(View.VISIBLE);
                            mLoginButton.setVisibility(View.VISIBLE);
                            mLoginLoader.setVisibility(View.INVISIBLE);

                            return;

                        } else {
                            if(MyLifecycleHandler.isApplicationInForeground()){
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myStatusMessage == null){
                                            Config.showDialogType1(LoginActivity.this, "Login Failed", myStatusMessage, "", null, true, "", "");
                                        } else {
                                            Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");
                                        }
                                        mEnterEmailTextView.setVisibility(View.VISIBLE);
                                        mEnterCodeTextView.setVisibility(View.INVISIBLE);
                                        mEmailEditTextHolder.setVisibility(View.VISIBLE);
                                        mEmailEditText.setVisibility(View.VISIBLE);
                                        mSendCodeButton.setVisibility(View.VISIBLE);
                                        mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                                        mPasscodeEditText.setVisibility(View.INVISIBLE);
                                        mLoginButton.setVisibility(View.INVISIBLE);
                                        mLoginLoader.setVisibility(View.INVISIBLE);

                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Login Failed.", Toast.LENGTH_SHORT).show();
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(MyLifecycleHandler.isApplicationInForeground()){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");

                                    mEnterEmailTextView.setVisibility(View.VISIBLE);
                                    mEnterCodeTextView.setVisibility(View.INVISIBLE);
                                    mEmailEditTextHolder.setVisibility(View.VISIBLE);
                                    mEmailEditText.setVisibility(View.VISIBLE);
                                    mSendCodeButton.setVisibility(View.VISIBLE);
                                    mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                                    mPasscodeEditText.setVisibility(View.INVISIBLE);
                                    mLoginButton.setVisibility(View.INVISIBLE);
                                    mLoginLoader.setVisibility(View.INVISIBLE);

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                if (MyLifecycleHandler.isApplicationInForeground()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");

                            mEnterEmailTextView.setVisibility(View.VISIBLE);
                            mEnterCodeTextView.setVisibility(View.INVISIBLE);
                            mEmailEditTextHolder.setVisibility(View.VISIBLE);
                            mEmailEditText.setVisibility(View.VISIBLE);
                            mSendCodeButton.setVisibility(View.VISIBLE);
                            mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                            mPasscodeEditText.setVisibility(View.INVISIBLE);
                            mLoginButton.setVisibility(View.INVISIBLE);
                            mLoginLoader.setVisibility(View.INVISIBLE);

                        }
                    });
                } else {
                    Log.e("PSignup anError: ", anError.getMessage());
                    Toast.makeText(getApplicationContext(), "Login Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void verifyLoginCode(String emailAddress, String passcode) {

        Log.e("PSignup Verify emailAddress", emailAddress);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // UI ACTIONS COME HERE
                mEnterEmailTextView.setVisibility(View.INVISIBLE);
                mEnterCodeTextView.setVisibility(View.VISIBLE);
                mEmailEditTextHolder.setVisibility(View.INVISIBLE);
                mEmailEditText.setVisibility(View.INVISIBLE);
                mSendCodeButton.setVisibility(View.INVISIBLE);
                mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                mPasscodeEditText.setVisibility(View.INVISIBLE);
                mLoginButton.setVisibility(View.INVISIBLE);
                mLoginLoader.setVisibility(View.VISIBLE);
            }
        });

        AndroidNetworking.post(Config.LINK_VERIFY_LOGIN_CODE)
                .addBodyParameter("user_email", emailAddress)
                .addBodyParameter("user_passcode", passcode)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("login_activity_send_verify_code")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.e("PSignup", response);

                if(!LoginActivity.this.isFinishing()){
                    Log.e("PSignup", "here 0");
                    try {
                        Log.e("PSignup", "here 1");
                        JSONObject o = new JSONObject(response);
                        String myStatus = o.getString("status");
                        final String myStatusMessage = o.getString("message");

                        if (myStatus.equalsIgnoreCase("success")) {
                            Log.e("PSignup", "here 2");

                            //STORING THE USER DATA
                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL, o.getString("user_email"));
                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID, o.getString("user_id"));
                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN, o.getString("access_token"));

                            Config.openActivity(LoginActivity.this, MainActivity.class, 1, 2, 0, "", "");
                            return;

                        } else {
                            Log.e("PSignup", "here 3");
                                Log.e("PSignup", "here 4");
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("PSignup", "here 5");
                                        if(myStatusMessage == null){
                                            Config.showDialogType1(LoginActivity.this, "Login Failed", myStatusMessage, "", null, true, "", "");
                                        } else {
                                            Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");
                                        }
                                        mEmailEditTextHolder.setVisibility(View.VISIBLE);
                                        mEmailEditText.setVisibility(View.VISIBLE);
                                        mSendCodeButton.setVisibility(View.VISIBLE);
                                        mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                                        mPasscodeEditText.setVisibility(View.INVISIBLE);
                                        mLoginButton.setVisibility(View.INVISIBLE);
                                        mLoginLoader.setVisibility(View.INVISIBLE);

                                    }
                                });

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");
                                    mEmailEditTextHolder.setVisibility(View.VISIBLE);
                                    mEmailEditText.setVisibility(View.VISIBLE);
                                    mSendCodeButton.setVisibility(View.VISIBLE);
                                    mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                                    mPasscodeEditText.setVisibility(View.INVISIBLE);
                                    mLoginButton.setVisibility(View.INVISIBLE);
                                    mLoginLoader.setVisibility(View.INVISIBLE);

                                }
                            });
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                if (!LoginActivity.this.isFinishing()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Config.showDialogType1(LoginActivity.this, "Login Failed", "Failed to sign you in", "", null, true, "", "");
                            mEmailEditTextHolder.setVisibility(View.VISIBLE);
                            mEmailEditText.setVisibility(View.VISIBLE);
                            mSendCodeButton.setVisibility(View.VISIBLE);
                            mPasscodeEditTextHolder.setVisibility(View.INVISIBLE);
                            mPasscodeEditText.setVisibility(View.INVISIBLE);
                            mLoginButton.setVisibility(View.INVISIBLE);
                            mLoginLoader.setVisibility(View.INVISIBLE);

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}