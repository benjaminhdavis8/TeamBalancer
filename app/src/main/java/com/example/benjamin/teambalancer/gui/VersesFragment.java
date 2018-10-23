package com.example.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendData;
import com.example.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VersesFragment extends Fragment {
    private LinearLayout team1View;
    private LinearLayout team2View;
    private List<Friend> team1List;
    private List<Friend> team2List;
    private List<Friend> bestTeam1;
    private List<Friend> bestTeam2;

    private List<Friend> players;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_verses, container, false);

        team1List = new ArrayList<>();
        team2List = new ArrayList<>();

        team1View = view.findViewById(R.id.team1);
        team2View = view.findViewById(R.id.team2);

        team1List = new ArrayList<>();
        team2List = new ArrayList<>();


        players = ((MainActivity)getActivity()).getPlayerList();;

        //Balance(players);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        newBalance(players);
//                    }
//                });
//            }
//        }).start();


        //PrintToTeam(team1View, team1List);
        //PrintToTeam(team2View, team2List);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

//        Balance(players);
//
        newBalance2(players);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        newBalance(players);
//                    }
//                });
//            }
//        }).start();
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

        PrintToTeam(team1View, team1List);
        PrintToTeam(team2View, team2List);

    }

    private void newBalance(List<Friend> players) {

        int team1MMR = 0;
        int team2MMR = 0;
        int bestdiff = 99999;
        int currdiff = 0;

        for(int n = 0; n < 100; n++)
        {
            //Shuffle players and put on both teams
            Collections.shuffle(players, new Random());
            team1List = new ArrayList<>();
            team2List = new ArrayList<>();
            int i = 0;
            while (i < players.size()/2) {
                team2List.add(players.get(i*2));
                team1List.add(players.get((i*2)+1));
                i++;
            }
            if (players.size()%2 == 1) {
                team1List.add(players.get(i*2));
            }

            //Check difference in MMR, keep best choice
            for (int y = 0; y < team1List.size(); y++) {
                team1MMR += team1List.get(y).getMMR();
            }
            for (int z = 0; z < team2List.size(); z++) {
                team2MMR += team2List.get(z).getMMR();
            }
            currdiff = (team1MMR > team2MMR) ? team1MMR - team2MMR : team2MMR - team1MMR;
            if (currdiff < bestdiff) {
                bestdiff = currdiff;
                bestTeam1 = new ArrayList<>(team1List);
                bestTeam2 = new ArrayList<>(team2List);
                PrintToTeam(team1View, bestTeam1);
                PrintToTeam(team2View, bestTeam2);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void newBalance2(List<Friend> players) {

        int team1MMR = 0;
        int team2MMR = 0;
        int bestdiff = 99999;
        int currdiff = 0;

        for(int n = 0; n < 100; n++)
        {
            team1MMR = 0;
            team2MMR = 0;
            //Shuffle players and put on both teams
            Collections.shuffle(players, new Random());
            team1List = new ArrayList<>();
            team2List = new ArrayList<>();
            int i = 0;
            while (i < players.size()/2) {
                team2List.add(players.get(i*2));
                team1List.add(players.get((i*2)+1));
                i++;
            }
            if (players.size()%2 == 1) {
                team1List.add(players.get(i*2));
            }

            //Check difference in MMR, keep best choice
            for (int y = 0; y < team1List.size(); y++) {
                team1MMR += team1List.get(y).getMMR();
            }
            for (int z = 0; z < team2List.size(); z++) {
                team2MMR += team2List.get(z).getMMR();
            }
            currdiff = (team1MMR > team2MMR) ? team1MMR - team2MMR : team2MMR - team1MMR;
            if (currdiff < bestdiff) {
                bestdiff = currdiff;
                bestTeam1 = new ArrayList<>(team1List);
                bestTeam2 = new ArrayList<>(team2List);
                PrintToTeam(team1View, bestTeam1);
                PrintToTeam(team2View, bestTeam2);
            }
        }

    }

    private void PrintToTeam(LinearLayout teamView, List<Friend> team) {
        teamView.removeAllViews();
        for (Friend f : team) {
            addPlayer(teamView, f);
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
                android:orientation="horizontal">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:background="@color/colorPrimaryTint"
                    android:layout_marginBottom="0dp"
                    android:layout_marginEnd="10dp"
                    android:layout_marginStart="0dp"
                    android:layout_marginTop="10dp"
                    android:paddingLeft="5dp"
                    android:paddingTop="5dp"
                    android:paddingRight="5dp"
                    android:paddingBottom="5dp">

                    <TextView
                        android:id="@+id/vs_username"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:maxLines="1"
                        android:text="USERNAME" />

                    <TextView
                        android:id="@+id/vs_rank"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:text="rank"
                        android:maxLines="1"
                        android:textColor="@android:color/darker_gray" />

                </LinearLayout>
            </LinearLayout>
     *
     */
    private void addPlayer(ViewGroup team, Friend friend) {
        LinearLayout view = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setLayoutParams(params);

        LinearLayout background = new LinearLayout(getActivity());
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        params.gravity= Gravity.CENTER_VERTICAL | Gravity.START;
        background.setLayoutParams(params);
        background.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint));
        background.setOrientation(LinearLayout.VERTICAL);
        background.setPadding(5, 10, 5, 20);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(getActivity());
        text.setLayoutParams(params);
        text.setText(friend.getUsername());
        text.setMaxLines(1);
        background.addView(text);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text = new TextView(getActivity());
        text.setLayoutParams(params);
        text.setText(friend.getRank().toString());
        text.setTextColor(friend.getRankColor(getContext()));
        text.setMaxLines(1);
        background.addView(text);

        view.addView(background);
        team.addView(view);
    }
}
