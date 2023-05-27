package com.tafarri.tafarri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.ImmutableList;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;
import com.tafarri.tafarri.Util.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EbookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackImageview, mReloadImageview, mBookCoverImageView, mChooseBuyBookFullImageView, mChooseBuyBookSummaryImageView;
    private TextView mReferenceTextView, mBookTitleTextView, mBookAuthorTextView, mBookPriceTextView, mBookLongDescriptionTextView,
            mChooseBuyBookFullTextView, mChooseBuyBookSummaryTextView, mSummaryBookTextView, mBuyOrReadInfoTextView;
    private RadioButton mMaleGenderRadioButton, mFemaleGenderRadioButton;
    private ConstraintLayout mPaymentItemHolderConstraintLayout;
    private Button mReadSummaryButton, mReadPaidFullBookButton, mReadPaidSummaryButton;
    private ProgressBar mLoadingProgressbar, mBuyLoadingProgressbar;
    private Boolean networkRequestStarted = false;
    int getting = 0;
    private String bookOrSummary = "";
    private Thread network_thread = null;

    BillingClient billingClient;
    TextView txt_coins;
    List<ProductDetails> productDetailsList;
    Activity activity;
    Prefs prefs;
    Toolbar toolbar;
    Handler handler;
    ArrayList<Integer> coins;
    ArrayList<String> productIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_details);
        initViews();

        mBackImageview = findViewById(R.id.activity_ebookdetails_fp_back_imageview);
        mReloadImageview = findViewById(R.id.reloadbook_imageview);
        mLoadingProgressbar = findViewById(R.id.loading_progressbar2);
        mBookCoverImageView = findViewById(R.id.list_item_book_image_imageview);
        mBookTitleTextView = findViewById(R.id.list_item_book_title_textview);
        mBuyLoadingProgressbar = findViewById(R.id.activity_ebookdetails_loading_progressbar);
        mBookAuthorTextView = findViewById(R.id.list_item_book_author_textview);
        mBookLongDescriptionTextView = findViewById(R.id.list_item_book_short_description_textview);
        mBookPriceTextView = findViewById(R.id.list_item_book_price_textview);
        mSummaryBookTextView = findViewById(R.id.list_item_booksummary_price_textview);
        mReadPaidFullBookButton = findViewById(R.id.activity_ebookdetails_readpaidfullbook_button);
        mReadPaidSummaryButton = findViewById(R.id.activity_ebookdetails_readpaidsummarybook_button);
        mPaymentItemHolderConstraintLayout = findViewById(R.id.buy_items_holder_constraintlayout);
        mReferenceTextView = findViewById(R.id.activity_ebookdetails_reference_textview);
        mReadSummaryButton = findViewById(R.id.activity_ebookdetails_readsummary_button);
        mBuyOrReadInfoTextView = findViewById(R.id.activity_ebookdetails_cedi_info_textview);
        mChooseBuyBookFullImageView = findViewById(R.id.fragment_signup_personalstage1_gender_male_imageview);
        mChooseBuyBookFullTextView = findViewById(R.id.fragment_signup_personalstage1_gender_male_textview);
        mMaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_male_radiobutton);
        mChooseBuyBookSummaryImageView = findViewById(R.id.fragment_signup_personalstage1_gender_female_imageview);
        mChooseBuyBookSummaryTextView = findViewById(R.id.fragment_signup_personalstage1_gender_female_textview);
        mFemaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_female_radiobutton);

        if(!EbookDetailsActivity.this.isFinishing() && !Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim().equalsIgnoreCase("")
        ){
            Config.loadImageView(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim(), mBookCoverImageView, null);
        }
        mBookTitleTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
        mBookLongDescriptionTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION).trim());
        mBookAuthorTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_AUTHOR).trim());
        mReferenceTextView.setText("Reference: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_ID).trim());

        /*
        if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim().equalsIgnoreCase("")){
            mBookPriceTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim() + " - Full Book");
        } else {
            mBookPriceTextView.setVisibility(View.GONE);
        }
         */
        if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("")){
            mSummaryBookTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim() + " - Summary");
        } else {
            mSummaryBookTextView.setVisibility(View.GONE);
        }


        mChooseBuyBookFullImageView.setOnClickListener(this);
        mChooseBuyBookFullTextView.setOnClickListener(this);
        mChooseBuyBookSummaryImageView.setOnClickListener(this);
        mChooseBuyBookSummaryTextView.setOnClickListener(this);
        mBackImageview.setOnClickListener(this);
        mReadPaidFullBookButton.setOnClickListener(this);
        mReadPaidSummaryButton.setOnClickListener(this);
        mReferenceTextView.setOnClickListener(this);
        mReadSummaryButton.setOnClickListener(this);
        mMaleGenderRadioButton.setOnClickListener(this);
        mFemaleGenderRadioButton.setOnClickListener(this);

        /*
        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL).trim().equalsIgnoreCase("")){
            mChooseBuyBookFullImageView.setVisibility(View.INVISIBLE);
            mChooseBuyBookFullTextView.setVisibility(View.INVISIBLE);
            //mMaleGenderRadioButton.setVisibility(View.INVISIBLE);
        } else {
            mChooseBuyBookFullImageView.setVisibility(View.VISIBLE);
            mChooseBuyBookFullTextView.setVisibility(View.VISIBLE);
            //mFemaleGenderRadioButton.setVisibility(View.VISIBLE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL).trim().equalsIgnoreCase("")){
            mChooseBuyBookSummaryImageView.setVisibility(View.INVISIBLE);
            mChooseBuyBookSummaryTextView.setVisibility(View.INVISIBLE);
            //mFemaleGenderRadioButton.setVisibility(View.INVISIBLE);
        } else {
            mChooseBuyBookSummaryImageView.setVisibility(View.VISIBLE);
            mChooseBuyBookSummaryTextView.setVisibility(View.VISIBLE);
            //mFemaleGenderRadioButton.setVisibility(View.VISIBLE);
        }
         */

        if(
                Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED).trim().equalsIgnoreCase("yes")
                || Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim().equalsIgnoreCase("Free")

        ){
            mReadPaidFullBookButton.setVisibility(View.VISIBLE);
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "EBOOKDETAILS_PURCHASED_PAGE");
            //Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            //Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL));
        } else {
            mReadPaidFullBookButton.setVisibility(View.GONE);
        }

        Config.show_log_in_console("BooksListAct", "\n E-DETAILS SHARED-P TITLE: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
        Config.show_log_in_console("BooksListAct", "\n E-DETAILS SHARED-P FULL AVAIL: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED));
        Config.show_log_in_console("BooksListAct", "\n E-DETAILS SHARED-P SUMMARY AVAIL: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED));
        if(
                Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED).trim().equalsIgnoreCase("yes")
                || Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("Free")
        ){
            mReadPaidSummaryButton.setVisibility(View.VISIBLE);
            mBuyOrReadInfoTextView.setText("You can read this summary on our website");
            mReadSummaryButton.setText("READ ON WEB");
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "EBOOKDETAILS_PURCHASED_PAGE");
            //Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            //Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL));
        } else {
            mReadPaidSummaryButton.setVisibility(View.GONE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED).trim().equalsIgnoreCase("yes") && Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED).trim().equalsIgnoreCase("yes")){
            mPaymentItemHolderConstraintLayout.setVisibility(View.GONE);
        } else {
            mPaymentItemHolderConstraintLayout.setVisibility(View.VISIBLE);
        }

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    verifyPurchase(purchase);
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        connectGooglePlayBilling();

    } // OnCreate END


    void connectGooglePlayBilling() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    showProducts();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")

    void showProducts() {
        String thisProductId = "one_dollar_summary";
        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$1.00")){
            thisProductId = "one_dollar_summary";
        } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$2.00")){
            thisProductId = "two_dollar_summary";
        } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$3.00")){
            thisProductId = "three_dollar_summary";
        } else {
            Toast.makeText(getApplicationContext(), "Payment error. Please contact support.", Toast.LENGTH_LONG).show();
            return;
        }
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(thisProductId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, list) -> {
            productDetailsList.clear();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Config.show_log_in_console("BILLING", "post delayed");
                    productDetailsList.addAll(list);
                    Config.show_log_in_console("BILLING", productDetailsList.size() + " number of products");
                }
            }, 2000);
        });
    }

    void launchPurchaseFlow(ProductDetails productDetails) {

        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    void verifyPurchase(Purchase purchase) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        ConsumeResponseListener listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String currentDateandTime = sdf.format(new Date());
                recordPayment(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_ID).trim(), "book_summary", "google", currentDateandTime, purchase.getOrderId());
                Config.show_log_in_console("BILLING", "verifyPurchase 2");
                //Toast.makeText(getApplicationContext(), "Purchase Completed", Toast.LENGTH_LONG).show();
            }
        };

        billingClient.consumeAsync(consumeParams, listener);


    }


    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                Config.show_log_in_console("BILLING", "verifyPurchase 1");
                                verifyPurchase(purchase);

                            }
                        }
                    }
                }
        );

    }



    @SuppressLint("SetTextI18n")
    void giveUserCoins(Purchase purchase) {

        Log.d("TestINAPP", purchase.getProducts().get(0));
        Log.d("TestINAPP", purchase.getQuantity() + " Quantity");

        for(int i=0;i<productIds.size();i++){
            if(purchase.getProducts().get(0).equals(productIds.get(i))){
                Config.show_log_in_console("BILLING", "Show User The Book");

            }
        }
    }



    @SuppressLint("SetTextI18n")
    private void initViews() {

        handler = new Handler();
        activity = this;
        prefs = new Prefs(this);


        productDetailsList = new ArrayList<>();

        productIds = new ArrayList<>();
        Config.show_log_in_console("BILLING", "SHARED_PREF_KEY_BOOK_SUMMARY_PRICE: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim());

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$1.00")){
            productIds.add("one_dollar_summary");
        } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$2.00")){
            productIds.add("two_dollar_summary");
        } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE).trim().equalsIgnoreCase("$3.00")){
            productIds.add("three_dollar_summary");
        } else {
            Toast.makeText(getApplicationContext(), "Payment error. Please contact support..", Toast.LENGTH_LONG).show();
        }
    }


    public void setMaleClicked(){
        bookOrSummary = "book_full";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "book_full");
        //mFemaleGenderRadioButton.setChecked(false);
    }

    public void setFemaleClicked(){
        bookOrSummary = "book_summary";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "book_summary");
        //mMaleGenderRadioButton.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        } else if(view.getId() == mReadPaidFullBookButton.getId()){
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK, "book_full");
            Intent intent = new Intent(getApplicationContext(), BookTextReaderActivity.class);
            startActivity(intent);
        } else if(view.getId() == mReadPaidSummaryButton.getId()){
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK, "book_summary");
            //Intent intent = new Intent(getApplicationContext(), BookTextReaderActivity.class);
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            startActivity(intent);
        } else if(view.getId() == mReferenceTextView.getId()){
            // USING PDF VIEWER
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_REFERENCE_URL).trim());
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT,Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
            startActivity(Intent.createChooser(intent, "Share"));
        } else if(view.getId() == mReadSummaryButton.getId()){
            if(
                    Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED).trim().equalsIgnoreCase("yes")
            ){
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, Config.LINK_WEB_HOW_TO_VIEW);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Access Your Books");
                startActivity(Intent.createChooser(intent, "Share"));
            } else {
                mReadSummaryButton.setVisibility(View.GONE);
                mBuyLoadingProgressbar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(productDetailsList.size() > 0){
                            launchPurchaseFlow(productDetailsList.get(0));
                            Config.show_log_in_console("BILLING", "getProductId: " + productDetailsList.get(0).getProductId());
                        } else {
                            Toast.makeText(getApplicationContext(), "Payment portal not ready. Please try again in 1 a minute", Toast.LENGTH_LONG).show();
                            mBuyLoadingProgressbar.setVisibility(View.GONE);
                            mReadSummaryButton.setVisibility(View.VISIBLE);
                        }
                    }
                }, 2000);
                /*
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_REFERENCE_URL).trim());
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
                startActivity(Intent.createChooser(intent, "Share"));
                 */
            }
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_radiobutton){
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_textview){
            //mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_imageview){
            //mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_radiobutton){
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_textview){
            //mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_imageview){
            //mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        }
    }



    private void recordPayment(final String itemID, String itemType, String paymentType, String paymentDate, String paymentRefNum){
        networkRequestStarted = true;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                //mLoadingProgressbar.setVisibility(View.VISIBLE);
            }
        });
        Config.show_log_in_console("getFinalPriceSummary", "token: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN).trim());
        Config.show_log_in_console("getFinalPriceSummary", "itemID: " + itemID);
        Config.show_log_in_console("getFinalPriceSummary", "itemType: " + itemType);
        Config.show_log_in_console("getFinalPriceSummary", "paymentType: " + paymentType);
        Config.show_log_in_console("getFinalPriceSummary", "paymentDate: " + paymentDate);
        Config.show_log_in_console("getFinalPriceSummary", "paymentRefNum: " + paymentRefNum);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LINK_RECORD_PAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Config.show_log_in_console("EbookDetailsAct", "response: " +  response);
                        if(!EbookDetailsActivity.this.isFinishing()){
                            try {
                                JSONObject response_json_object = new JSONObject(response);
                                String message = response_json_object.getString("message");

                                if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {


                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                            Config.openActivity(EbookDetailsActivity.this, BookTextReaderActivity.class, 1, 2, 0, "", "");
                                            return;
                                        }
                                    });

                                } else {
                                    //mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();

                                //mLoadingProgressbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();

                        //mLoadingProgressbar.setVisibility(View.INVISIBLE);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN).trim());
                //headers.put("ContentType", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("user_email", Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL).trim());
                map.put("item_id", itemID);
                map.put("item_type", itemType);
                map.put("payment_type", paymentType);
                map.put("payment_ref_number", paymentRefNum);
                map.put("payment_date", paymentDate);
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