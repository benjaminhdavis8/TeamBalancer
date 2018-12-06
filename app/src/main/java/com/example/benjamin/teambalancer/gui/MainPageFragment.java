package com.example.benjamin.teambalancer.gui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendsList;
import com.example.benjamin.teambalancer.R;

import java.util.List;

public class MainPageFragment extends Fragment {

    LinearLayout friendsButton;
    LinearLayout teamButton;
    ConstraintLayout backarrowLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        backarrowLayout = getActivity().findViewById(R.id.backarrow_layout);

        List<Friend> playerlist = ((MainActivity) getActivity()).getPlayerList();
        playerlist = FriendsList.getInstance(getContext()).getFriends();
        for (Friend f: playerlist)
        {
            f.setSelected(false);
        }

        friendsButton = view.findViewById(R.id.friends_layout);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToFriendsFragment();
            }
        });

        teamButton = view.findViewById(R.id.team_layout);
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToSelectFragment();
            }
        });

        return view;
    }


    public void onResume(){
        super.onResume();

        backarrowLayout.setVisibility(View.INVISIBLE);
        backarrowLayout.setClickable(false);
    }

    @Override
    public void onPause(){
        super.onPause();

        //backarrowLayout.setVisibility(View.VISIBLE);
        //backarrowLayout.setClickable(true);
    }

}
