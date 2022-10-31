package com.fishpott.dita.Util;

import android.app.Activity;
import android.content.Intent;

import com.fishpott.dita.R;

public class Config {


    public static void openActivity(Activity thisActivity, Class NewActivity, int includeAnimation, int finishActivity, int addData, String dataIndex, String dataValue) {
        Intent intent = new Intent(thisActivity, NewActivity);
        if(addData == 1){
            intent.putExtra(dataIndex, dataValue);
        }

        if(finishActivity == 1){
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else if(finishActivity == 2){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else {
            thisActivity.startActivity(intent);
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        }
        thisActivity = null;
    }
}
