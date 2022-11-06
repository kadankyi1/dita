package com.fishpott.dita.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpott.dita.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EbooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EbooksFragment extends Fragment {


    public EbooksFragment() {
        // Required empty public constructor
    }

    public static EbooksFragment newInstance() {
        EbooksFragment fragment = new EbooksFragment();
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
        return inflater.inflate(R.layout.fragment_ebooks, container, false);
    }
}