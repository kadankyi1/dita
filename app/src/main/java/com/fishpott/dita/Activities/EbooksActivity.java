package com.fishpott.dita.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fishpott.dita.ListDataGenerators.BooksListDataGenerator;
import com.fishpott.dita.Models.BookModel;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EbooksActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView m_back_imageview, m_reload_imageview;
    private ProgressBar m_loading_progressbar;
    private RecyclerView m_recyclerview;
    private LinearLayoutManager m_linearlayoutmanager;
    int getting = 0;
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebooks);

        m_back_imageview = findViewById(R.id.fragment_about_fp_back_imageview);
        m_reload_imageview = findViewById(R.id.reloadbooks_imageview);
        m_loading_progressbar = findViewById(R.id.loading_progressbar);
        m_recyclerview = findViewById(R.id.books_holder_recyclerview);


        m_linearlayoutmanager = new LinearLayoutManager(EbooksActivity.this);

        m_recyclerview.setItemViewCacheSize(20);
        m_recyclerview.setDrawingCacheEnabled(true);
        m_recyclerview.setHasFixedSize(true);
        m_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        m_recyclerview.setLayoutManager(m_linearlayoutmanager);
        m_recyclerview.setAdapter(new RecyclerViewAdapter());

        network_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                call_audio_list_api("Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN));
            }
        });
        network_thread.start();

        m_reload_imageview.setOnClickListener(this);
        m_back_imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        } else if(view.getId() == m_reload_imageview.getId()){
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api("Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN));
                }
            });
            network_thread.start();
        }
    }



    private void allOnClickHandlers(View view, int position){
        if(view.getId() == R.id.list_item_book_parent_holder){
            Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
            startActivity(intent);
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);
            vh = new AudioViewHolder(v);

            return vh;
        }


        public class AudioViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout m_parent_holder_constraintlayout, m_image_holder_constraintlayout;
            private AppCompatImageView m_audio_image;
            private TextView m_title_textview, m_price_textview;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public AudioViewHolder(View v) {
                super(v);
                m_parent_holder_constraintlayout = v.findViewById(R.id.list_item_book_parent_holder);
                m_image_holder_constraintlayout = v.findViewById(R.id.list_item_book_image_constraintlayout);
                m_audio_image = v.findViewById(R.id.list_item_book_image_imageview);
                m_title_textview = v.findViewById(R.id.list_item_book_title_textview);
                m_price_textview = v.findViewById(R.id.list_item_book_price_textview);

                m_parent_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_image_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_audio_image.setOnClickListener(innerClickListener);
                m_title_textview.setOnClickListener(innerClickListener);
                m_price_textview.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            if(!EbooksActivity.this.isFinishing() && !BooksListDataGenerator.getAllData().get(position).getBook_cover_photo().equalsIgnoreCase("")){

                Config.loadImageView(getApplicationContext(), BooksListDataGenerator.getAllData().get(position).getBook_cover_photo().trim(), ((AudioViewHolder) holder).m_audio_image, null);

            } else {
                ((AudioViewHolder) holder).m_audio_image.setImageResource(R.drawable.cover);
            }
            ((AudioViewHolder) holder).m_title_textview.setText(BooksListDataGenerator.getAllData().get(position).getBook_title());
            ((AudioViewHolder) holder).m_price_textview.setText(BooksListDataGenerator.getAllData().get(position).getBook_cost());

        }

        @Override
        public int getItemCount() {
            return BooksListDataGenerator.getAllData().size();
        }

    }


    private void call_audio_list_api(final String token){

        if(!this.isFinishing() && getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_reload_imageview.setVisibility(View.INVISIBLE);
                    m_recyclerview.setVisibility(View.INVISIBLE);
                    m_loading_progressbar.setVisibility(View.VISIBLE);
                    BooksListDataGenerator.getAllData().clear();
                    m_recyclerview.getAdapter().notifyDataSetChanged();
                }
            });

            Config.show_log_in_console("AudiosListAct", "\n token: " + token);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LINK_VERIFY_GET_BOOKS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Config.show_log_in_console("AudiosListAct", "response: " +  response);
                            if(!EbooksActivity.this.isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONArray linkupsSuggestionsArray = response_json_object.getJSONArray("data");

                                        Config.show_log_in_console("AudiosListAct", "linkupsSuggestionsArray: " + linkupsSuggestionsArray.toString());
                                        if (linkupsSuggestionsArray.length() > 0) {
                                            BooksListDataGenerator.getAllData().clear();

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_recyclerview.getAdapter().notifyDataSetChanged();
                                                }
                                            });
                                            for (int i = 0; i < linkupsSuggestionsArray.length(); i++) {
                                                BookModel mine1 = new BookModel();
                                                final JSONObject k = linkupsSuggestionsArray.getJSONObject(i);
                                                mine1.setBook_id(k.getLong("book_id"));
                                                mine1.setBook_sys_id(k.getString("book_sys_id"));
                                                mine1.setBook_title(k.getString("book_title"));
                                                mine1.setBook_author(k.getString("book_author"));
                                                mine1.setBook_ratings(k.getString("book_ratings"));
                                                mine1.setBook_cover_photo(k.getString("book_cover_photo"));
                                                mine1.setBook_description_short(k.getString("book_description_short"));
                                                mine1.setBook_description_long(k.getString("book_description_long"));
                                                mine1.setBook_pages(k.getString("book_pages"));
                                                mine1.setBook_pdf(k.getString("book_pdf"));
                                                mine1.setBook_summary_pdf(k.getString("book_summary_pdf"));
                                                mine1.setBook_audio(k.getString("book_audio"));
                                                mine1.setBook_summary_audio(k.getString("book_summary_audio"));
                                                mine1.setBook_cost(k.getString("book_cost_usd"));
                                                //mine1.setCreated_at(k.getString("created_at"));
                                                BooksListDataGenerator.addOneData(mine1);

                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!EbooksActivity.this.isFinishing() && m_recyclerview != null) {
                                                            m_recyclerview.getAdapter().notifyItemInserted(BooksListDataGenerator.getAllData().size());
                                                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                                                            m_reload_imageview.setVisibility(View.INVISIBLE);
                                                            m_recyclerview.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        } else {

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                                    m_recyclerview.setVisibility(View.INVISIBLE);
                                                    m_reload_imageview.setVisibility(View.VISIBLE);
                                                    Toast.makeText(getApplicationContext(), "No Audios found", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    } else {
                                        m_recyclerview.setVisibility(View.INVISIBLE);
                                        m_loading_progressbar.setVisibility(View.INVISIBLE);
                                        m_reload_imageview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    m_loading_progressbar.setVisibility(View.INVISIBLE);
                                    m_recyclerview.setVisibility(View.INVISIBLE);
                                    m_reload_imageview.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            m_loading_progressbar.setVisibility(View.INVISIBLE);
                            m_recyclerview.setVisibility(View.INVISIBLE);
                            m_reload_imageview.setVisibility(View.VISIBLE);
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("kw", "");
                    map.put("app_type", "ANDROID");
                    map.put("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())));
                    Config.show_log_in_console("LoginActivity", "Map: " +  map.toString());
                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", token);
                    //headers.put("ContentType", "application/json");
                    return headers;
                }


                /*
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("user_phone_number", phone_number);
                    map.put("password", password);
                    Util.show_log_in_console("LoginActivity", "Map: " +  map.toString());
                    return map;
                }
                 */
            };
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}
