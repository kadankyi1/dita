package com.tafarri.tafarri.Util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tafarri.tafarri.Activities.ReaderWebViewActivity;
import com.tafarri.tafarri.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Config {

    // DEBUG
    //public static Boolean ALLOW_LOGGING = true;
    //public static final String CURRENT_HTTP_IN_USE = "https://";
    //public static final String CURRENT_ENVIRONMENT_DOMAIN_IN_USE = "dita.local";

    // LIVE
    public static Boolean ALLOW_LOGGING = true;
    public static final String CURRENT_HTTP_IN_USE = "https://";
    public static final String CURRENT_ENVIRONMENT_DOMAIN_IN_USE = "tafarri.com";

    public static final String LINK_WEB_HOW_TO_VIEW = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/reader";
    public static final String LINK_SEND_LOGIN_CODE = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/send-login-code";
    public static final String LINK_VERIFY_LOGIN_CODE = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/verify-login-code";
    public static final String LINK_GET_BOOKS = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/get-books";
    public static final String LINK_GET_MY_BOOKS = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/get-my-books";
    public static final String LINK_CONTACT_DITA_TEAM = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/send-message";
    public static final String LINK_RECORD_PAYMENT = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/record-app-payment";


    public static final String WEBVIEW_KEY_URL = "URL";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL = "USER_EMAIL";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID = "USER_ID";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN = "USER_PASSWORD";


    public static final String SHARED_PREF_KEY_BOOK_POSITION_IN_LIST = "SHARED_PREF_KEY_BOOK_POSITION_IN_LIST";
    public static final String SHARED_PREF_KEY_BOOK_ID = "SHARED_PREF_KEY_BOOK_ID";
    public static final String SHARED_PREF_KEY_BOOK_COVER_URL = "SHARED_PREF_KEY_BOOK_COVER_URL";
    public static final String SHARED_PREF_KEY_BOOK_TITLE = "SHARED_PREF_KEY_BOOK_TITLE";
    public static final String SHARED_PREF_KEY_BOOK_AUTHOR = "SHARED_PREF_KEY_BOOK_AUTHOR";
    public static final String SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION = "SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION";
    public static final String SHARED_PREF_KEY_BOOK_PRICE = "SHARED_PREF_KEY_BOOK_PRICE";
    public static final String SHARED_PREF_KEY_BOOK_SUMMARY_PRICE = "SHARED_PREF_KEY_BOOK_SUMMARY_PRICE";
    public static final String SHARED_PREF_KEY_BOOK_FULL_URL = "SHARED_PREF_KEY_BOOK_FULL_URL";
    public static final String SHARED_PREF_KEY_BOOK_SUMMARY_URL = "SHARED_PREF_KEY_BOOK_SUMMARY_URL";
    public static final String SHARED_PREF_KEY_BOOK_FULL_PURCHASED = "SHARED_PREF_KEY_BOOK_FULL_PURCHASED";
    public static final String SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED = "SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED";
    public static final String SHARED_PREF_KEY_BOOK_REFERENCE_URL = "SHARED_PREF_KEY_BOOK_REFERENCE_URL";
    public static final String SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED = "SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED";
    public static final String SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK = "SHARED_PREF_KEY_READING_FULL_OR_SUMMARY";


    public static final String SHARED_PREF_KEY_LAST_READING_PDF_URL = "SHARED_PREF_KEY_LAST_READING_PDF_URL";
    public static final String SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME = "SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME";
    public static final String SHARED_PREF_KEY_READING_FROM = "SHARED_PREF_KEY_READING_FROM";


    public static void show_log_in_console(String title, String description){
        if(ALLOW_LOGGING){
            Log.e(title, description);
        }
    }

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


    // GET SHARED PREFERENCE STRING
    public static String getSharedPreferenceString(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getString(key, "");

    }

    // EDIT SHARED PREFERENCE STRING
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        context = null;
    }

    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceInt(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceFloat(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceFloat(Context context, String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE BOOLEAN
    public static boolean getSharedPreferenceBoolean(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getBoolean(key, false);

    }

    // SET SHARED PREFERENCE BOOLEAN
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        context = null;
        editor.apply();
    }

    // CLEAR ALL SHARED PREFERENCE
    public static void deleteAllDataInSharedPreference(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        context = null;
    }

    // GET APP VERSION CODE
    public static int getAppVersionCode(Context context){
        PackageInfo pinfo = null;
        int currentVersionNumber = 0;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionNumber = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionNumber;
    }


    // DIALOG TYPE 1 SHOWS AN OKAY BUTTON THAT DOES NOTHING
    public static Dialog.OnCancelListener showDialogType1(final Activity thisActivity, String subTitle, String subBody, String subBody2, Dialog.OnCancelListener cancelListener, Boolean canNotBeClosedFromOutSideClick, String positiveButtonText, String negativeButtonText){

        if(thisActivity != null) {
            final Dialog dialog = new Dialog(thisActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            if (canNotBeClosedFromOutSideClick) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
            if (subBody2.trim().equalsIgnoreCase("show-positive-image")) {
                dialog.setContentView(R.layout.positive_icon_dialog);
            } else {
                dialog.setContentView(R.layout.login_activity_dialog);
            }

            TextView dialogTextView = dialog.findViewById(R.id.login_activity_dialog_text);
            TextView dialogTextView2 = dialog.findViewById(R.id.login_activity_dialog_text2);
            ImageView dialogTextImageView = dialog.findViewById(R.id.login_activity_dialog_imageview);
            dialogTextView.setText(subBody);

            if (subBody.trim().equalsIgnoreCase("")) {
                dialogTextView.setVisibility(View.INVISIBLE);
            } else {
                dialogTextView.setText(subBody);
            }

            if (subBody2.trim().equalsIgnoreCase("") || subBody2.trim().equalsIgnoreCase("show-positive-image")) {
                dialogTextView2.setVisibility(View.INVISIBLE);
            } else {
                dialogTextView2.setText(subBody2);
            }

            if (subTitle.trim().equalsIgnoreCase("1")) {
                dialogTextImageView.setVisibility(View.GONE);
            }

            dialogTextView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = CURRENT_HTTP_IN_USE + "www.fishpott.com/service_agreements.html";
                    openActivity(thisActivity, ReaderWebViewActivity.class, 1, 0, 1, WEBVIEW_KEY_URL, url);
                }
            });

            AppCompatButton positiveDialogButton = dialog.findViewById(R.id.login_activity_dialog_button);
            AppCompatButton negativeDialogButton = dialog.findViewById(R.id.login_activity_dialog_button_cancel);
            if (!positiveButtonText.trim().equalsIgnoreCase("")) {
                positiveDialogButton.setText(positiveButtonText);
            }
            if (!negativeButtonText.trim().equalsIgnoreCase("")) {
                negativeDialogButton.setText(negativeButtonText);
            } else {
                negativeDialogButton.setVisibility(View.GONE);
            }
            positiveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            negativeDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.setOnCancelListener(cancelListener);
            return cancelListener;
        } else {
            return null;
        }
    }


    // CHECK IF USER IS LOGGED IN
    public static Boolean userIsLoggedIn(Activity thisActivity){
        if(!getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL).trim().equalsIgnoreCase("") && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID).trim().equalsIgnoreCase("") && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN).trim().equalsIgnoreCase("")){
            return true;
        } else {
           return false;
        }
    }


    public static void loadImageView(Context context, String url, ImageView imageView, final ProgressBar progressBar){

        if(context != null && imageView != null){
            Config.show_log_in_console("loadImageView", "url: " + url);
            Glide.with(context)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if(progressBar != null){
                                progressBar.setVisibility(View.GONE);
                            }
                            Config.show_log_in_console("loadImageView", "onLoadFailed: " + e.toString());
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if(progressBar != null){
                                progressBar.setVisibility(View.GONE);
                            }
                            Config.show_log_in_console("loadImageView", "onResourceReady");
                            return false;
                        }
                    })
                    .into(imageView);
            Config.show_log_in_console("loadImageView", "COMPLETED");
        }
    }



    //GETTING THE COMPONENTS OF A URL
    public static String getUrlComponent(String u, int getType){
        URL url = null;
        String componentReturned = "";
        try {
            url = new URL(u);
            if(getType == 1){
                //GETTING DOMAIN NAME/HOST
                componentReturned = url.getHost();
            } else if(getType == 2){
                //GETTING PROTOCOL
                componentReturned = url.getProtocol();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return componentReturned;
    }

    // REPLACE WWW. AND HTTP IN URL
    public static String removeWwwAndHttpFromUrl(String url){
        return url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
    }


    public static String removeCharacters(String text, String allRemovingCharacters){
        return text.replaceAll("[" + allRemovingCharacters + "]", "");
    }


    public static String getCurrentDateTime() {
        // get date time in custom format
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
        return sdf.format(new Date());
    }

    public static String getCurrentDateTime2() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public static String getCurrentDateTime3(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // GETTING A MONTH NAME(FULL/SHORT) FROM IT'S NUMBER
    public static String getMonthNameFromMonthNumber(Activity thisActivity, int monthNumber, int nameType){
        String[] allMonths;

        if(nameType == 1){
            allMonths = thisActivity.getResources().getStringArray(R.array.fragment_signup_personalstage2_month_names_full);
        } else {
            allMonths = thisActivity.getResources().getStringArray(R.array.fragment_signup_personalstage2_month_names_short);
        }
        return allMonths[monthNumber];
    }

    // OPENING THE DATE PICKER DIALOG BOX
    public static DatePickerDialog.OnDateSetListener  openDatePickerDialog(Activity thisActivity, DatePickerDialog.OnDateSetListener mDateSetListener, Boolean dateIsPreset, int thisDay, int thisMonth, int thisYear){

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(dateIsPreset){
            year = thisYear;
            month = thisMonth;
            day = thisDay;
        }

        DatePickerDialog dialog = new DatePickerDialog(thisActivity, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return  mDateSetListener;
    }
}
