package com.tafarri.tafarri.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

public class ReaderWebViewActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView mUrlTextView;
    private ImageView mHttpsLockImageView, mBackImageView, mReloadBookImageView;
    private WebView mWebView;
    private ProgressBar mPageLoadingProgressBar, mPageLoadingProgressBar2;
    private String websiteUrl = "", domainName = "";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_web_view);

        // BINDING VIEWS
        mUrlTextView = findViewById(R.id.activity_webview_constraint2_title_textview);
        mHttpsLockImageView = findViewById(R.id.activity_webview_padlock_imageView);
        mWebView = findViewById(R.id.activity_webview_webview);
        mBackImageView = findViewById(R.id.activity_webview_back_imageview);
        mPageLoadingProgressBar = findViewById(R.id.activity_webview_loader);
        mReloadBookImageView = findViewById(R.id.reloadbooks_imageview);
        mPageLoadingProgressBar2 = findViewById(R.id.loading_progressbar);
        handler = new Handler();

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("PAYMENT_PAGE")) {
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK).trim().equalsIgnoreCase("book_full")){
                websiteUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL).trim();
                Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, websiteUrl);
            } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK).trim().equalsIgnoreCase("book_summary")){
                websiteUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL).trim();
                Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, websiteUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Book type verification failed", Toast.LENGTH_LONG).show();
            }
        } else if(
                Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("SETTINGS_PAGE")
                        || Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("EBOOKDETAILS_PURCHASED_PAGE")
        ) {
            if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim().equalsIgnoreCase("")){
                websiteUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim();
            } else {
                Toast.makeText(getApplicationContext(), "Reading continuation failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Book verification failed", Toast.LENGTH_LONG).show();
        }

        Config.show_log_in_console("BILLING", "websiteUrl: " + websiteUrl);

        if(!websiteUrl.trim().equalsIgnoreCase("")) {
            websiteUrl = "https://docs.google.com/gview?embedded=true&url=" + websiteUrl;
            //websiteUrl =(String) getIntent().getExtras().get(Config.WEBVIEW_KEY_URL);
            Config.show_log_in_console("websiteUrl", websiteUrl);
            domainName = Config.getUrlComponent(websiteUrl, 1);
            domainName = Config.removeWwwAndHttpFromUrl(domainName);
            mPageLoadingProgressBar.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "Book type verification failed...", Toast.LENGTH_LONG).show();
            finish();
        }

        if(domainName.trim().equalsIgnoreCase("")){
            //mUrlTextView.setText("External Link");
        } else {
            //mUrlTextView.setText(domainName);
        }

        /*
        if(Config.getUrlComponent(websiteUrl, 2).trim().equalsIgnoreCase("https")){
            mHttpsLockImageView.setImageResource(R.drawable.webview_activity_lock_image);
            mHttpsLockImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mHttpsLockImageView.setImageResource(R.drawable.webview_activity_open_lock);
            mHttpsLockImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }
         */

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl(websiteUrl);
        mWebView.setWebChromeClient(new WebChromeClient());

        mBackImageView.setOnClickListener(this);
        mReloadBookImageView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_webview_back_imageview){
            onBackPressed();
        } else if(view.getId() == mReloadBookImageView.getId()){
            mWebView.reload();
            mReloadBookImageView.setVisibility(View.GONE);
            mPageLoadingProgressBar2.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mReloadBookImageView.setVisibility(View.VISIBLE);
                    mPageLoadingProgressBar2.setVisibility(View.GONE);
                }
            }, 5000);
        }
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mPageLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mPageLoadingProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }



}