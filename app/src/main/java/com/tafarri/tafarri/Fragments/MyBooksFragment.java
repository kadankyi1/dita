package com.tafarri.tafarri.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.tafarri.tafarri.Activities.EbookDetailsActivity;
import com.tafarri.tafarri.Activities.EbooksActivity;
import com.tafarri.tafarri.ListDataGenerators.MyBooksListDataGenerator;
import com.tafarri.tafarri.Models.BookModel;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyBooksFragment extends Fragment implements View.OnClickListener {

    private ImageView mReloadImageview;
    private ProgressBar mLoadingProgressbar;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearlayoutmanager;
    int getting = 0;
    private String keyword = "";
    private Thread network_thread = null;

    public MyBooksFragment() {
        // Required empty public constructor
    }

    public static MyBooksFragment newInstance() {
        MyBooksFragment fragment = new MyBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_books, container, false);


        mReloadImageview = view.findViewById(R.id.reloadbooks_imageview);
        mLoadingProgressbar = view.findViewById(R.id.loading_progressbar);
        mRecyclerview = view.findViewById(R.id.books_holder_recyclerview);

        mLinearlayoutmanager = new LinearLayoutManager(getContext());

        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setDrawingCacheEnabled(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerview.setLayoutManager(mLinearlayoutmanager);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());

        network_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                call_audio_list_api("Bearer " + Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN), keyword);
            }
        });
        network_thread.start();

        mReloadImageview.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mReloadImageview.getId()){
            network_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    call_audio_list_api("Bearer " + Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN), keyword);
                }
            });
            network_thread.start();
        }
    }



    private void allOnClickHandlers(View view, int position){
        if(view.getId() == R.id.list_item_book_parent_holder){
            Config.setSharedPreferenceInt(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_POSITION_IN_LIST, position);
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_ID, MyBooksListDataGenerator.getAllData().get(position).getBook_sys_id());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL, MyBooksListDataGenerator.getAllData().get(position).getBook_cover_photo());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE, MyBooksListDataGenerator.getAllData().get(position).getBook_title());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_AUTHOR, MyBooksListDataGenerator.getAllData().get(position).getBook_author());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION, MyBooksListDataGenerator.getAllData().get(position).getBook_description_long());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE, MyBooksListDataGenerator.getAllData().get(position).getBook_cost());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PRICE, MyBooksListDataGenerator.getAllData().get(position).getBook_summary_cost());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL, MyBooksListDataGenerator.getAllData().get(position).getBook_pdf());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL, MyBooksListDataGenerator.getAllData().get(position).getBook_summary_pdf());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED, MyBooksListDataGenerator.getAllData().get(position).getBook_full_purchased());

            Config.show_log_in_console("BooksListActNOW", "\n List getBook_summary_pdf: " + MyBooksListDataGenerator.getAllData().get(position).getBook_summary_pdf());
            Config.show_log_in_console("BooksListActNOW", "\n List getBook_pdf: " + MyBooksListDataGenerator.getAllData().get(position).getBook_pdf());
            Config.show_log_in_console("BooksListAct", "\n List getBook_title: " + MyBooksListDataGenerator.getAllData().get(position).getBook_title());
            Config.show_log_in_console("BooksListAct", "\n List getBook_summary_purchased: " + MyBooksListDataGenerator.getAllData().get(position).getBook_summary_purchased());
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED, MyBooksListDataGenerator.getAllData().get(position).getBook_summary_purchased());
            Config.show_log_in_console("BooksListAct", "\n SHARED-P: " + Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED));

            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_REFERENCE_URL, MyBooksListDataGenerator.getAllData().get(position).getBook_reference_url());
            Intent intent = new Intent(getActivity().getApplicationContext(), EbookDetailsActivity.class);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mybook, parent, false);
            vh = new RecyclerViewAdapter.AudioViewHolder(v);

            return vh;
        }


        public class AudioViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout m_parent_holder_constraintlayout, m_image_holder_constraintlayout;
            private AppCompatImageView m_audio_image;
            private TextView m_title_textview, m_price_textview, m_author_textview, m_short_desc_textview;

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
                m_author_textview = v.findViewById(R.id.list_item_book_author_textview);
                m_short_desc_textview = v.findViewById(R.id.list_item_book_short_description_textview);

                m_parent_holder_constraintlayout.setOnClickListener(innerClickListener);
                //m_image_holder_constraintlayout.setOnClickListener(innerClickListener);
                //m_audio_image.setOnClickListener(innerClickListener);
                //m_title_textview.setOnClickListener(innerClickListener);
                //m_price_textview.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            if(!getActivity().isFinishing() && !MyBooksListDataGenerator.getAllData().get(position).getBook_cover_photo().equalsIgnoreCase("")){

                Config.loadImageView(getActivity().getApplicationContext(), MyBooksListDataGenerator.getAllData().get(position).getBook_cover_photo().trim(), ((RecyclerViewAdapter.AudioViewHolder) holder).m_audio_image, null);

            } else {
                ((RecyclerViewAdapter.AudioViewHolder) holder).m_audio_image.setImageResource(R.drawable.cover);
            }
            ((RecyclerViewAdapter.AudioViewHolder) holder).m_title_textview.setText(MyBooksListDataGenerator.getAllData().get(position).getBook_title());
            ((RecyclerViewAdapter.AudioViewHolder) holder).m_author_textview.setText(MyBooksListDataGenerator.getAllData().get(position).getBook_author());
            ((RecyclerViewAdapter.AudioViewHolder) holder).m_short_desc_textview.setText(MyBooksListDataGenerator.getAllData().get(position).getBook_description_short());
            ((RecyclerViewAdapter.AudioViewHolder) holder).m_price_textview.setText(MyBooksListDataGenerator.getAllData().get(position).getBook_summary_cost());

        }

        @Override
        public int getItemCount() {
            return MyBooksListDataGenerator.getAllData().size();
        }

    }


    private void call_audio_list_api(final String token, final String kw){

        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mReloadImageview.setVisibility(View.INVISIBLE);
                    mRecyclerview.setVisibility(View.INVISIBLE);
                    mLoadingProgressbar.setVisibility(View.VISIBLE);
                    MyBooksListDataGenerator.getAllData().clear();
                    mRecyclerview.getAdapter().notifyDataSetChanged();
                }
            });

            Config.show_log_in_console("BooksListAct", "\n token: " + token);
            Config.show_log_in_console("BooksListAct", "\n keyword: " + kw);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LINK_GET_MY_BOOKS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Config.show_log_in_console("BooksListAct", "response: " +  response);
                            if(!getActivity().isFinishing()){
                                try {
                                    JSONObject response_json_object = new JSONObject(response);

                                    if(response_json_object.getString("status").equalsIgnoreCase("success")){
                                        JSONArray linkupsSuggestionsArray = response_json_object.getJSONArray("data");

                                        Config.show_log_in_console("BooksListAct", "linkupsSuggestionsArray: " + linkupsSuggestionsArray.toString());
                                        if (linkupsSuggestionsArray.length() > 0) {
                                            MyBooksListDataGenerator.getAllData().clear();

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mRecyclerview.getAdapter().notifyDataSetChanged();
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
                                                mine1.setBook_summary_cost(k.getString("book_summary_cost_usd"));
                                                mine1.setBook_full_purchased(k.getString("book_full_purchased"));
                                                mine1.setBook_summary_purchased(k.getString("book_summary_purchased"));
                                                mine1.setBook_reference_url(k.getString("book_reference_url"));
                                                //mine1.setCreated_at(k.getString("created_at"));

                                                //Config.show_log_in_console("BooksListAct", "Title: " + mine1.getBook_title() + " -- book_full_purchased: " +  mine1.getBook_full_purchased() + " -- book_summary_purchased: " +  mine1.getBook_summary_purchased());
                                                MyBooksListDataGenerator.addOneData(mine1);

                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!getActivity().isFinishing() && mRecyclerview != null) {
                                                            mRecyclerview.getAdapter().notifyItemInserted(MyBooksListDataGenerator.getAllData().size());
                                                            mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                                            mReloadImageview.setVisibility(View.INVISIBLE);
                                                            mRecyclerview.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        } else {

                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                                    mRecyclerview.setVisibility(View.INVISIBLE);
                                                    mReloadImageview.setVisibility(View.VISIBLE);
                                                    //Toast.makeText(getActivity().getApplicationContext(), "No Books found", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    } else {
                                        mRecyclerview.setVisibility(View.INVISIBLE);
                                        mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                        mReloadImageview.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity().getApplicationContext(), response_json_object.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity().getApplicationContext(), "An unexpected error occurred.", Toast.LENGTH_LONG).show();
                                    mLoadingProgressbar.setVisibility(View.INVISIBLE);
                                    mRecyclerview.setVisibility(View.INVISIBLE);
                                    mReloadImageview.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Config.show_log_in_console("BooksListAct", "error: " +  error.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_LONG).show();
                            mLoadingProgressbar.setVisibility(View.INVISIBLE);
                            mRecyclerview.setVisibility(View.INVISIBLE);
                            mReloadImageview.setVisibility(View.VISIBLE);
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("kw", kw);
                    map.put("app_type", "ANDROID");
                    map.put("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())));
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

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}