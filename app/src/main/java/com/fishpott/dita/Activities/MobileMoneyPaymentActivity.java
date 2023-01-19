package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileMoneyPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackImageView;
    private ConstraintLayout mContentHolderConstraintLayout;
    private EditText mTransactionIdEditText, mAmountSentEditText, mSenderNameEditText;
    private TextView mReceivingPhoneNumberTextView, mReceivingAccNameTextView, mTitleTextView;
    private TextView mDateMonthTextView, mDateDayTextView, mDateYearTextView, mAmtTextView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextInputLayout mAmountSentTextInputLayout;
    private Dialog.OnCancelListener cancelListenerActive1;
    private ProgressBar mLoadingProgressbar;
    private TextView mLoaderTextView;
    private Button mSendButton;
    private Thread networkRequestThread = null;
    private Boolean REQUEST_HAS_STARTED = false, networkRequestStarted = false;
    private int presetDateDay = 19, presetDateMonth = 6, presetDateYear = 2019;
    private String momoType = "", momoNumber = "", momoName = "", dob = "";
    private String amountCedis = "";
    private int paymentGatewayPriceInCentsOrPesewas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_money_payment);

        mBackImageView = findViewById(R.id.title_bar_back_icon_imageview);
        mContentHolderConstraintLayout = findViewById(R.id.contents_holder);
        mTitleTextView = findViewById(R.id.country_withdrawfunds_activity_textview);
        mReceivingPhoneNumberTextView = findViewById(R.id.receiving_phone_number_textview);
        mReceivingAccNameTextView = findViewById(R.id.receiving_phone_number_acc_name_textview);
        mAmtTextView = findViewById(R.id.amount_info_textview);
        mTransactionIdEditText = findViewById(R.id.transaction_id_edittext);
        mAmountSentEditText = findViewById(R.id.amount_sent_edittext);
        mAmountSentTextInputLayout = findViewById(R.id.amount_sent_textinputlayout);
        mSenderNameEditText = findViewById(R.id.sender_name_edittext);
        mDateYearTextView = findViewById(R.id.date_year_textview);
        mDateMonthTextView = findViewById(R.id.date_month_textview);
        mDateDayTextView = findViewById(R.id.date_day_textview);
        mSendButton = findViewById(R.id.request_button);
        mLoadingProgressbar = findViewById(R.id.activity_login_loader);

        // LOADER
        //mLoaderImageView = findViewById(R.id.loader_imageview);
        mLoaderTextView = findViewById(R.id.loadertext_textview);

        mAmountSentTextInputLayout.setHint("Amount(Ghc)");
        mAmtTextView.setText("Amount : " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE));

        presetDateYear = Integer.valueOf(Config.getCurrentDateTime3("yyyy")).intValue();
        presetDateMonth = (Integer.valueOf(Config.getCurrentDateTime3("MM")).intValue()) - 1;
        presetDateDay = Integer.valueOf(Config.getCurrentDateTime3("dd")).intValue();

        mTitleTextView.setText("Send the full payment of " + amountCedis + " to the Account below");
        mReceivingPhoneNumberTextView.setText("Phone Number: " + momoNumber);
        mReceivingAccNameTextView.setText("Account Name: " + momoName);

        cancelListenerActive1 = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Config.openActivity(MobileMoneyPaymentActivity.this, BookTextReaderActivity.class, 1, 1, 0, "", "");
            }
        };

        // LISTENING FOR WHEN THE DATE IS SET AND SETTING THE DATE TEXT
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDateYearTextView.setText(String.valueOf(year));
                mDateMonthTextView.setText(Config.getMonthNameFromMonthNumber(MobileMoneyPaymentActivity.this, month, 2));
                mDateDayTextView.setText(String.valueOf(dayOfMonth));
                presetDateDay = dayOfMonth;
                presetDateMonth = month;
                presetDateYear = year;
                dob = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
            }
        };

        mBackImageView.setOnClickListener(this);
        mDateYearTextView.setOnClickListener(this);
        mDateMonthTextView.setOnClickListener(this);
        mDateDayTextView.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_bar_back_icon_imageview) {
            onBackPressed();
        } else if (view.getId() == R.id.date_year_textview
                || view.getId() == R.id.date_month_textview
                || view.getId() == R.id.date_day_textview) {
            mDateSetListener = Config.openDatePickerDialog(MobileMoneyPaymentActivity.this, mDateSetListener, true, presetDateDay, presetDateMonth, presetDateYear);
        } else if(view.getId() == R.id.request_button){
            if(!dob.trim().equalsIgnoreCase("")
                    && !mTransactionIdEditText.getText().toString().trim().equalsIgnoreCase("")){
                final String transID = mTransactionIdEditText.getText().toString().trim();
                final String amount = mAmountSentEditText.getText().toString().trim();
                final String senderName = mSenderNameEditText.getText().toString().trim();
                networkRequestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateOrderStatus("Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_ID), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED), "momo", dob, transID);
                    }
                });
                networkRequestThread.start();
            } else {
                Toast.makeText(getApplicationContext(), "The form is incomplete", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void updateOrderStatus(final String token, final String itemID, String itemType, String paymentType, String paymentDate, String paymentRefNum){
        networkRequestStarted = true;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                mLoaderTextView.setText("Recording payment...");
                mContentHolderConstraintLayout.setVisibility(View.INVISIBLE);
                mLoadingProgressbar.setVisibility(View.VISIBLE);
                mLoaderTextView.setVisibility(View.VISIBLE);
                //mLoaderImageView.startAnimation(AnimationUtils.loadAnimation(MobileMoneyPaymentActivity.this, R.anim.suggestion_loading_anim));
            }
        });
        Config.show_log_in_console("getFinalPriceSummary", "token: " + token);
        Config.show_log_in_console("getFinalPriceSummary", "itemID: " + itemID);
        Config.show_log_in_console("getFinalPriceSummary", "itemType: " + itemType);
        Config.show_log_in_console("getFinalPriceSummary", "paymentType: " + paymentType);
        Config.show_log_in_console("getFinalPriceSummary", "paymentDate: " + paymentDate);
        Config.show_log_in_console("getFinalPriceSummary", "paymentRefNum: " + paymentRefNum);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LINK_RECORD_PAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Config.show_log_in_console("ContactDitaAct", "response: " +  response);
                        if(!MobileMoneyPaymentActivity.this.isFinishing()){
                            try {
                                JSONObject response_json_object = new JSONObject(response);
                                String message = response_json_object.getString("message");

                                if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {


                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK, itemType);
                                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "PAYMENT_PAGE");
                                            Config.openActivity(MobileMoneyPaymentActivity.this, BookTextReaderActivity.class, 1, 2, 0, "", "");
                                            return;
                                        }
                                    });

                                } else {
                                    mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                    mLoaderTextView.setVisibility(View.INVISIBLE);
                                    mSendButton.setVisibility(View.VISIBLE);
                                    mContentHolderConstraintLayout.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();

                                mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                mLoaderTextView.setVisibility(View.INVISIBLE);
                                mSendButton.setVisibility(View.VISIBLE);
                                mContentHolderConstraintLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();

                        mLoadingProgressbar.setVisibility(View.INVISIBLE);
                        mLoaderTextView.setVisibility(View.INVISIBLE);
                        mSendButton.setVisibility(View.VISIBLE);
                        mContentHolderConstraintLayout.setVisibility(View.VISIBLE);
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
                map.put("item_id", itemID);
                map.put("item_type", itemType);
                map.put("payment_type", paymentType);
                map.put("payment_date", paymentDate);
                map.put("payment_ref_number", paymentRefNum);
                map.put("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())));
                map.put("app_type", "android");
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
