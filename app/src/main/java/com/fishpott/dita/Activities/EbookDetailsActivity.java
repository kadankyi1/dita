package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fishpott.dita.ListDataGenerators.BooksListDataGenerator;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

public class EbookDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackImageview, mReloadImageview, mBookCoverImageView;
    private TextView mBookTitleTextView, mBookAuthorTextView, mBookPriceTextView, mBookLongDescriptionTextView;
    private RadioButton mMaleGenderRadioButton, mFemaleGenderRadioButton;
    private Button mReadFullButton, mReadSummaryButton;
    private ProgressBar mLoadingProgressbar;
    int getting = 0;
    private String bookOrSummary = "";
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_details);

        mBackImageview = findViewById(R.id.activity_ebookdetails_fp_back_imageview);
        mReloadImageview = findViewById(R.id.reloadbook_imageview);
        mLoadingProgressbar = findViewById(R.id.loading_progressbar);
        mBookCoverImageView = findViewById(R.id.list_item_book_image_imageview);
        mBookTitleTextView = findViewById(R.id.list_item_book_title_textview);
        mBookAuthorTextView = findViewById(R.id.list_item_book_author_textview);
        mBookLongDescriptionTextView = findViewById(R.id.list_item_book_short_description_textview);
        mBookPriceTextView = findViewById(R.id.list_item_book_price_textview);
        mReadFullButton = findViewById(R.id.activity_ebookdetails_readfull_button);
        mReadSummaryButton = findViewById(R.id.activity_ebookdetails_readsummary_button);
        mMaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_male_radiobutton);
        mFemaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_female_radiobutton);

        if(!EbookDetailsActivity.this.isFinishing() && !Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim().equalsIgnoreCase("")
        ){
            Config.loadImageView(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim(), mBookCoverImageView, null);
        }
        mBookTitleTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
        mBookLongDescriptionTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION).trim());
        mBookAuthorTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_AUTHOR).trim());
        mBookPriceTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim());


        findViewById(R.id.fragment_signup_personalstage1_gender_male_imageview).setOnClickListener(this);
        findViewById(R.id.fragment_signup_personalstage1_gender_male_textview).setOnClickListener(this);
        findViewById(R.id.fragment_signup_personalstage1_gender_female_imageview).setOnClickListener(this);
        findViewById(R.id.fragment_signup_personalstage1_gender_female_textview).setOnClickListener(this);
        mBackImageview.setOnClickListener(this);
        mReadFullButton.setOnClickListener(this);
        mReadSummaryButton.setOnClickListener(this);
        mMaleGenderRadioButton.setOnClickListener(this);
        mFemaleGenderRadioButton.setOnClickListener(this);

    }


    public void setMaleClicked(){
        bookOrSummary = "BOOK";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "BOOK");
        mFemaleGenderRadioButton.setChecked(false);
    }

    public void setFemaleClicked(){
        bookOrSummary = "SUMMARY";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "SUMMARY");
        mMaleGenderRadioButton.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        } else if(view.getId() == mReadFullButton.getId()){
            // USING PDF VIEWER
            if(!bookOrSummary.trim().equalsIgnoreCase("")){
                Intent intent = new Intent(getApplicationContext(), MobileMoneyPaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Choose if you are buying a full book or a summary", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == mReadSummaryButton.getId()){
            if(!bookOrSummary.trim().equalsIgnoreCase("")){
                Intent intent = new Intent(getApplicationContext(), MobileMoneyPaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Choose if you are buying a full book or a summary", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_radiobutton){
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_textview){
            mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_imageview){
            mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_radiobutton){
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_textview){
            mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_imageview){
            mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        }
    }
}