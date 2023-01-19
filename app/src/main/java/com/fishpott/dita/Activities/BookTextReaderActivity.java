package com.fishpott.dita.Activities;

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
import android.widget.Toast;

import com.fishpott.dita.ListDataGenerators.BooksListDataGenerator;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class BookTextReaderActivity extends AppCompatActivity {

    private ImageView mBackImageview;
    private String bookFullUrl = "", bookSummaryUrl = "", downloadFolder = "";
    private PDFView pdfView;
    private Thread downloadThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text_reader);

        mBackImageview = findViewById(R.id.activity_webview_back_imageview);
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
        Config.show_log_in_console("BookTextReaderTest", "bookFullUrl: " + bookFullUrl);

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


        /*
        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                downLoadPDF2(bookFullUrl, downloadFolder, "reading.pdf");
            }
        });
        downloadThread.start();

         */
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
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationUri(Uri.fromFile(result));
            //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(BookTextReaderActivity.this, new String[]{result.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    //Config.show_log_in_console("BookTextReaderTest", "Book Path: " + downloadFolder + "/cb.pdf");
                                    //Config.show_log_in_console("BookTextReaderTest", "Book uri: " + uri.toString());
                                    pdfView.fromUri(uri).load();
                                    Toast.makeText(getApplicationContext(), "Showing PDF", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
        } catch (Exception e) {
            Config.show_log_in_console("BookTextReaderTest", "3 ErrorDetail: " + e.toString());
            //Log.e(">>>>>", e.toString());
            //mToast(this, e.toString());
            return false;
        }
        return true;
    }


}