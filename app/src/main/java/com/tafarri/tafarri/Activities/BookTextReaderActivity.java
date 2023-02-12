package com.tafarri.tafarri.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;

public class BookTextReaderActivity extends AppCompatActivity {

    private ImageView mBackImageview;
    private String bookFullUrl = "", bookSummaryUrl = "", downloadFolder = "";
    private PDFView pdfView;
    private Uri bookPDFUri = null;
    private File bookPDFFile = null;
    private Thread downloadThread = null;
    private ProgressBar mGettingPDFLoader;
    private Handler timerHandler = new Handler();
    private boolean shouldRun = true, PDFIsShowing = false;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                /* Put your code here */

                File file = new File(Environment.getExternalStorageDirectory(), "Download");
                if (file.exists()) {
                    File result = new File(file.getAbsolutePath() + File.separator + Config.removeCharacters(bookFullUrl, ":/."));

                    MediaScannerConnection.scanFile(BookTextReaderActivity.this, new String[]{result.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Config.show_log_in_console("BookTextReaderTest", "Book Path: " + downloadFolder + "/cb.pdf");
                                            //Config.show_log_in_console("BookTextReaderTest", "pdfView.getPageCount() : " + pdfView.getPageCount());

                                            if(!PDFIsShowing && pdfView.getPageCount() > 0){
                                                if(mGettingPDFLoader != null){
                                                    mGettingPDFLoader.setVisibility(View.GONE);
                                                }
                                                shouldRun = false;
                                                PDFIsShowing = true;
                                            }
                                            if(!PDFIsShowing && pdfView.getPageCount() <= 0) {
                                                pdfView.fromUri(uri).load();
                                                //Toast.makeText(getApplicationContext(), "pdfView.getPageCount() : " + pdfView.getPageCount(), Toast.LENGTH_LONG).show();
                                                //Toast.makeText(getApplicationContext(), "Repeat check for Showing PDF.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });

                }
                //run again after 200 milliseconds (1/5 sec)
                timerHandler.postDelayed(this, 1000 * 30);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text_reader);

        mBackImageview = findViewById(R.id.activity_webview_back_imageview);
        mGettingPDFLoader = findViewById(R.id.activity_reader_loader);
        pdfView = findViewById(R.id.mypdfView);


        downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("PAYMENT_PAGE")) {
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK).trim().equalsIgnoreCase("book_full")){
                bookFullUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL).trim();
                Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, bookFullUrl);
            } else if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK).trim().equalsIgnoreCase("book_summary")){
                bookFullUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL).trim();
                Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, bookFullUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Book type verification failed", Toast.LENGTH_LONG).show();
            }
        } else if(
                Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("SETTINGS_PAGE")
                        || Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM).trim().equalsIgnoreCase("EBOOKDETAILS_PURCHASED_PAGE")
        ) {
            if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim().equalsIgnoreCase("")){
                bookFullUrl = Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim();
            } else {
                Toast.makeText(getApplicationContext(), "Reading continuation failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Book verification failed", Toast.LENGTH_LONG).show();
        }
        Config.show_log_in_console("BookTextReaderTestNOW", "bookFullUrl: " + bookFullUrl);

        mBackImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        try {
            if(downloadTask(bookFullUrl, Config.removeCharacters(bookFullUrl, ":/."))){
                //Config.show_log_in_console("BookTextReaderTest", "removeCharacters: " + Config.removeCharacters(bookFullUrl, ":/."));
                //pdfView.fromUri(Uri.parse(downloadFolder + "/.cb.pdf")).load();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get book", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Config.show_log_in_console("BookTextReaderTest", "3 ErrorDetail: Again");
        }

    }


    //In this example, the timer is started when the activity is loaded, but this need not to be the case
    @Override
    public void onResume() {
        super.onResume();
        /* ... */
        timerHandler.postDelayed(timerRunnable, 0);
    }

    //Stop task when the user quits the activity
    @Override
    public void onPause() {
        super.onPause();
        /* ... */
        shouldRun = false;
        timerHandler.removeCallbacksAndMessages(timerRunnable);
    }

    private boolean downloadTask(String url, String name) throws Exception {
        if (!url.startsWith("http")) {
            return false;
        }
        //String name = "cb.pdf";
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Download");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            File result = new File(file.getAbsolutePath() + File.separator + name);
            if(!result.exists()) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setDestinationUri(Uri.fromFile(result));
                bookPDFUri = Uri.fromFile(result);
                bookPDFFile = result;
                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                if (downloadManager != null) {
                    downloadManager.enqueue(request);
                }
                //mToast(mContext, "Starting download...");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Prepping your book.", Toast.LENGTH_LONG).show();
                    }
                });

                /*
                MediaScannerConnection.scanFile(BookTextReaderActivity.this, new String[]{result.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Config.show_log_in_console("BookTextReaderTest", "Book uri: " + uri.toString());
                                        if(!PDFIsShowing){
                                            PDFIsShowing =  true;
                                            pdfView.fromUri(uri).load();
                                            Toast.makeText(getApplicationContext(), "NEW downloaded file. Showing PDF.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    */
            } else {

                MediaScannerConnection.scanFile(BookTextReaderActivity.this, new String[]{result.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Config.show_log_in_console("BookTextReaderTest", "Book Path: " + downloadFolder + "/cb.pdf");
                                        //Config.show_log_in_console("BookTextReaderTest", "3 Book uri: " + uri.toString());
                                        if(!PDFIsShowing) {
                                            //PDFIsShowing =  true;
                                            pdfView.fromUri(uri).load();
                                            Toast.makeText(getApplicationContext(), "Prepping your book.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
            }

        } catch (Exception e) {
            Config.show_log_in_console("BookTextReaderTest", "3 ErrorDetail: " + e.toString());
            return false;
        }
        return true;
    }


}