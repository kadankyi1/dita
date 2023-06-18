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

public class WebViewActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView mUrlTextView;
    private ImageView mHttpsLockImageView, mBackImageView, mReloadBookImageView;
    private WebView mWebView;
    private ProgressBar mPageLoadingProgressBar, mPageLoadingProgressBar2;
    private String websiteUrl = "", domainName = "";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // BINDING VIEWS
        mUrlTextView = findViewById(R.id.activity_webview_constraint2_title_textview);
        mHttpsLockImageView = findViewById(R.id.activity_webview_padlock_imageView);
        mWebView = findViewById(R.id.activity_webview_webview);
        mBackImageView = findViewById(R.id.activity_webview_back_imageview);
        mPageLoadingProgressBar = findViewById(R.id.activity_webview_loader);
        mReloadBookImageView = findViewById(R.id.reloadbooks_imageview);
        mPageLoadingProgressBar2 = findViewById(R.id.loading_progressbar);
        handler = new Handler();

        //websiteUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_REFERENCE_URL).trim();
        websiteUrl =(String) getIntent().getExtras().get(Config.WEBVIEW_KEY_URL);

        Config.show_log_in_console("BILLING", "websiteUrl: " + websiteUrl);

        if(!websiteUrl.trim().equalsIgnoreCase("")) {
            Config.show_log_in_console("websiteUrl", websiteUrl);
            domainName = Config.getUrlComponent(websiteUrl, 1);
            domainName = Config.removeWwwAndHttpFromUrl(domainName);
            mPageLoadingProgressBar.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_LONG).show();
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
            //onBackPressed();
            finish();
        } else if(view.getId() == mReloadBookImageView.getId()){
            mWebView.reload();

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
            mPageLoadingProgressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mPageLoadingProgressBar.setVisibility(View.INVISIBLE);
            mPageLoadingProgressBar2.setVisibility(View.INVISIBLE);
            mReloadBookImageView.setVisibility(View.VISIBLE);
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