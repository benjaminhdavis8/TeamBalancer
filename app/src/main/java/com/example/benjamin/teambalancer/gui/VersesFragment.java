package com.example.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendData;
import com.example.benjamin.teambalancer.R;

import java.util.Collections;
import java.util.List;

public class VersesFragment extends Fragment {
    private LinearLayout team1View;
    private LinearLayout team2View;
    private List<Friend> team1List;
    private List<Friend> team2List;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_verses, container, false);

        team1View = view.findViewById(R.id.team1);
        team2View = view.findViewById(R.id.team2);

        List<Friend> players = ((MainActivity)getActivity()).getPlayerList();

        Balance(players);

        PrintToTeam(team1View, team1List);

        return view;
    }

    private void PrintToTeam(LinearLayout teamView, List<Friend> team) {
        for (Friend f :
                team) {
            addPlayer(teamView, f);
        }
    }

    private void Balance(List<Friend> players) {
        Collections.sort(players);
        int i = 0;
        while (i < players.size()/2) {
            team2List.add(players.get(i*2));
            team1List.add(players.get((i*2)+1));
            i++;
        }
        if (players.size()%2 == 1) {
            team1List.add(players.get(i*2));
        }

    }

    /*
     * Creates a new child lay out with content representing FriendData
     * and ads it to the team LinearLayout.
     *
     * The xml would look like this
     *
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                android:layout_marginBottom="50dp"
                android:layout_marginEnd="5dp"
                android:layout_marginStart="5dp"
                android:layout_marginTop="50dp"
                android:background="@color/colorPrimaryTint"
                android:orientation="vertical"
                android:padding="5dp">

                <TextView
                    android:id="@+id/vs_username"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="USERNAME" />

                <TextView
                    android:id="@+id/vs_rank"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="rank"
                    android:textColor="@android:color/darker_gray" />

            </LinearLayout>
     *
     */
    private void addPlayer(ViewGroup team, Friend friend) {
        LinearLayout view = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        params.setMargins(5, 50, 5, 50);
        view.setLayoutParams(params);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint));
        view.setOrientation(LinearLayout.VERTICAL);
        view.setPadding(5, 5, 5, 5);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(getActivity());
        text.setLayoutParams(params);
        text.setText(friend.getUsername());
        view.addView(text);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text = new TextView(getActivity());
        text.setLayoutParams(params);
        text.setText(friend.getRank().toString());
        view.addView(text);

        team.addView(view);
    }
}
