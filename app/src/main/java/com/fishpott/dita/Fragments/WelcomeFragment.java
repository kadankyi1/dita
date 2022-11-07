package com.fishpott.dita.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpott.dita.Activities.EbooksActivity;
import com.fishpott.dita.Activities.JourneysActivity;
import com.fishpott.dita.Activities.SubscriptionActivity;
import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

public class WelcomeFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout mEbooksHolderConstraintLayout, mSubscriptionHolderConstraintLayout, mJourneysHolderConstraintLayout;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        mEbooksHolderConstraintLayout = view.findViewById(R.id.fragment_about_ebooks_holder_contrainlayout);
        mSubscriptionHolderConstraintLayout = view.findViewById(R.id.subscription_holder_contrainlayout);
        mJourneysHolderConstraintLayout = view.findViewById(R.id.journeys_holder_contrainlayout);

        mEbooksHolderConstraintLayout.setOnClickListener(this);
        mJourneysHolderConstraintLayout.setOnClickListener(this);
        mSubscriptionHolderConstraintLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mEbooksHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), EbooksActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mJourneysHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), JourneysActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mSubscriptionHolderConstraintLayout.getId()){
            Config.openActivity(getActivity(), SubscriptionActivity.class, 0, 0, 0, "", "");
        }
    }
}