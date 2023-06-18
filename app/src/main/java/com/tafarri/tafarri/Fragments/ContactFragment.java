package com.tafarri.tafarri.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tafarri.tafarri.Activities.BookTextReaderActivity;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

public class ContactFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private ConstraintLayout mSubscriptionHolderConstraintLayout, mJourneysHolderConstraintLayout;
    private TextView mEmailTextView, mLastReadingBookNameTextView;
    private ImageView mSettingsIconImageView, mInfoImageView;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        mSubscriptionHolderConstraintLayout = view.findViewById(R.id.subscription_holder_contrainlayout);
        mLastReadingBookNameTextView = view.findViewById(R.id.subscription_textview);
        mEmailTextView = view.findViewById(R.id.myemail_textview);
        mSettingsIconImageView = view.findViewById(R.id.activity_mainactivity_constraintlayout2_menuicon_imageview);

        mEmailTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL));

        mSubscriptionHolderConstraintLayout.setOnClickListener(this);
        mSettingsIconImageView.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == mSubscriptionHolderConstraintLayout.getId()){
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@tafarri.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Tafarri App Enquiry");
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity().getApplicationContext(), "There are no email client installed on your device.",Toast.LENGTH_LONG);
            }
        } else if(view.getId() == mSettingsIconImageView.getId()){
            //mFragmentsHolderViewPager.setCurrentItem(0);
        }

    }
}