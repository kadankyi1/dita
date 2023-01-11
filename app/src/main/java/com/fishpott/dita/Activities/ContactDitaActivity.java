package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactDitaActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackImageview;
    private EditText mMessageEditttext;
    private Button mSendButton;
    private ProgressBar mLoadingProgressbar;
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_dita);

        mBackImageview =  findViewById(R.id.activity_contactdita_back_imageview);
        mMessageEditttext =  findViewById(R.id.contactactivity_message_edittext);
        mLoadingProgressbar =  findViewById(R.id.contactactivity_contentloading_progressbar);
        mSendButton =  findViewById(R.id.contactactivity_message_button);

        mBackImageview.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSendButton.getId()){
            String message = mMessageEditttext.getText().toString().trim();
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!message.equalsIgnoreCase("")){
                        call_api("Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN), message);
                    }
                }
            });
            network_thread.start();
        } else if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        }
    }


    private void call_api(final String token, final String the_message){

        if(!ContactDitaActivity.this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mMessageEditttext.setVisibility(View.INVISIBLE);
                    mSendButton.setVisibility(View.INVISIBLE);
                    mLoadingProgressbar.setVisibility(View.VISIBLE);
                }
            });

            Config.show_log_in_console("ContactDitaAct", "\n token: " + token);
            Config.show_log_in_console("ContactDitaAct", "\n the_message: " + the_message);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LINK_CONTACT_DITA_TEAM,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Config.show_log_in_console("ContactDitaAct", "response: " +  response);
                            if(!ContactDitaActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                                mSendButton.setVisibility(View.VISIBLE);
                                                mMessageEditttext.setVisibility(View.VISIBLE);
                                                mMessageEditttext.getText().clear();
                                                Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else {
                                        mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                        mSendButton.setVisibility(View.VISIBLE);
                                        mMessageEditttext.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                    mSendButton.setVisibility(View.VISIBLE);
                                    mMessageEditttext.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            mLoadingProgressbar.setVisibility(View.INVISIBLE);
                            mSendButton.setVisibility(View.VISIBLE);
                            mMessageEditttext.setVisibility(View.VISIBLE);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", token);
                    //headers.put("ContentType", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("message_text", the_message);
                    return map;
                }
            };
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}