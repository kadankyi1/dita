package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

public class EbookDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackImageview, mReloadImageview, mBookCoverImageView;
    private TextView mBookTitleTextView, mBookAuthorTextView, mBookPriceTextView, mBookLongDescriptionTextView;
    private AppCompatButton mReadFullButton, mReadSummaryButton;
    private ProgressBar mLoadingProgressbar;
    int getting = 0;
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

        if(!EbookDetailsActivity.this.isFinishing() && !Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim().equalsIgnoreCase("")
        ){
            Config.loadImageView(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim(), mBookCoverImageView, null);
        }
        mBookTitleTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
        mBookLongDescriptionTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION).trim());
        mBookAuthorTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_AUTHOR).trim());
        mBookPriceTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim());


        mBackImageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        }
    }
}