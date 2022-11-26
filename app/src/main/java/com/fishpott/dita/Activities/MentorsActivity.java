package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fishpott.dita.R;

public class MentorsActivity extends AppCompatActivity implements View.OnClickListener  {

    private ImageView mBackImageview, mReloadImageview;
    private ProgressBar mLoadingProgressbar;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearlayoutmanager;
    int getting = 0;
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);

        mBackImageview = findViewById(R.id.activity_journeys_fp_back_imageview);
        mReloadImageview = findViewById(R.id.reloadbooks_imageview);
        mLoadingProgressbar = findViewById(R.id.loading_progressbar);

        mReloadImageview.setOnClickListener(this);
        mBackImageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        } else if(view.getId() == mReloadImageview.getId()){
            /*
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api("Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN));
                }
            });
            network_thread.start();
             */
        }
    }
}