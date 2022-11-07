package com.fishpott.dita.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpott.dita.Activities.ContactDitaActivity;
import com.fishpott.dita.Activities.EbooksActivity;
import com.fishpott.dita.Activities.JourneysActivity;
import com.fishpott.dita.Activities.MentorsActivity;
import com.fishpott.dita.Activities.SubscriptionActivity;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private ConstraintLayout mSubscriptionHolderConstraintLayout, mJourneysHolderConstraintLayout, mEbooksHolderConstraintLayout,
            mMentorsHolderConstraintLayout, mContactHolderConstraintLayout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        mSubscriptionHolderConstraintLayout = view.findViewById(R.id.subscription_holder_contrainlayout);
        mJourneysHolderConstraintLayout = view.findViewById(R.id.journeys_holder_contrainlayout);
        mEbooksHolderConstraintLayout = view.findViewById(R.id.ebooks_holder_contrainlayout);
        mMentorsHolderConstraintLayout = view.findViewById(R.id.mentors_holder_contrainlayout);
        mContactHolderConstraintLayout = view.findViewById(R.id.contact_fp_holder_contrainlayout);

        mSubscriptionHolderConstraintLayout.setOnClickListener(this);
        mJourneysHolderConstraintLayout.setOnClickListener(this);
        mEbooksHolderConstraintLayout.setOnClickListener(this);
        mMentorsHolderConstraintLayout.setOnClickListener(this);
        mContactHolderConstraintLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSubscriptionHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), SubscriptionActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mJourneysHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), JourneysActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mEbooksHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), EbooksActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mMentorsHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), MentorsActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mContactHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), ContactDitaActivity.class, 0, 0, 0, "", "");
        }

    }
}