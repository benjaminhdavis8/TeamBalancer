package com.example.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.R;

public class MainPage extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        Button button = view.findViewById(R.id.frends_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToFrendsFragmen();
            }
        });

        button = view.findViewById(R.id.player_entry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToEntryFragment();
            }
        });

        return view;
    }
}
