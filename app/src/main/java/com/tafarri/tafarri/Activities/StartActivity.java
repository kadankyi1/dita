package com.tafarri.tafarri.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

public class StartActivity extends AppCompatActivity {

    AppCompatButton mStartAppCompatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        if(Config.userIsLoggedIn(StartActivity.this)) {
            Config.openActivity(StartActivity.this, MainActivity.class, 1, 2, 0, "", "");
            return;
        }

        findViewById(R.id.activity_start_start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.openActivity(StartActivity.this, LoginActivity.class, 1, 0, 0, "", "");
            }
        });
    }
}