package com.example.itread.Ui.fragment.collect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itread.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadedFragment extends Fragment {

    public ReadedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_readed, container, false);
    }
}
