package com.fishpott.dita.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpott.dita.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JourneysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JourneysFragment extends Fragment {

    public JourneysFragment() {
        // Required empty public constructor
    }

    public static JourneysFragment newInstance() {
        JourneysFragment fragment = new JourneysFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journeys, container, false);
    }
}