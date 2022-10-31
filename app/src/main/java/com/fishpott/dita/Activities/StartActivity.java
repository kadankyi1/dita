package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

public class StartActivity extends AppCompatActivity {

    AppCompatButton mStartAppCompatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.activity_start_start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.openActivity(StartActivity.this, LoginActivity.class, 1, 0, 0, "", "");
            }
        });
    }
}