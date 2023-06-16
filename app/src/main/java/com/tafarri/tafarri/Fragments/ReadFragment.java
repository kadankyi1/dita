package com.tafarri.tafarri.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tafarri.tafarri.Activities.ReaderWebViewActivity;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private ConstraintLayout mSubscriptionHolderConstraintLayout, mJourneysHolderConstraintLayout;
    private TextView mLastReadingBookNameTextView;
    private ImageView mSettingsIconImageView, mInfoImageView;

    public ReadFragment() {
        // Required empty public constructor
    }

    public static ReadFragment newInstance() {
        ReadFragment fragment = new ReadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_read, container, false);

        mSubscriptionHolderConstraintLayout = view.findViewById(R.id.subscription_holder_contrainlayout);
        mLastReadingBookNameTextView = view.findViewById(R.id.subscription_textview);
        mJourneysHolderConstraintLayout = view.findViewById(R.id.journeys_holder_contrainlayout);
        mSettingsIconImageView = view.findViewById(R.id.activity_mainactivity_constraintlayout2_menuicon_imageview);


        mSubscriptionHolderConstraintLayout.setOnClickListener(this);
        mSettingsIconImageView.setOnClickListener(this);
        //mInfoImageView.setOnClickListener(this);
        if(!Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME).trim().equalsIgnoreCase("") && !Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim().equalsIgnoreCase("")){
            mLastReadingBookNameTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME));
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSubscriptionHolderConstraintLayout.getId()){
            //Config.openActivity(getActivity(), SubscriptionActivity.class, 0, 0, 0, "", "");
            Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "SETTINGS_PAGE");
            if(!Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME).trim().equalsIgnoreCase("") && !Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim().equalsIgnoreCase("")){

                //Toast.makeText(getActivity().getApplicationContext(), Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim(), Toast.LENGTH_LONG).show();
                //Config.openActivity(getActivity(), BookTextReaderActivity.class, 0, 0, 0, "", "");
                Config.openActivity(getActivity(), ReaderWebViewActivity.class, 0, 0, 0, "", "");
            }
        } else if(view.getId() == mSettingsIconImageView.getId()){
            //mFragmentsHolderViewPager.setCurrentItem(0);
        }

    }
}