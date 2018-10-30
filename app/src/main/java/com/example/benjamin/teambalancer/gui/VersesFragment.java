package com.example.benjamin.teambalancer.gui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    ConstraintLayout backArrowLayout;

    private List<Friend> players;

    private Typeface friz;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_verses, container, false);

        team1List = new ArrayList<>();
        team2List = new ArrayList<>();

        team1View = view.findViewById(R.id.team1);
        team2View = view.findViewById(R.id.team2);

        team1List = new ArrayList<>();
        team2List = new ArrayList<>();

        friz = Typeface.createFromAsset(getContext().getAssets(), "fonts/friz.otf");

        backArrowLayout = getActivity().findViewById(R.id.backarrow_layout);

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

        backArrowLayout.setVisibility(View.VISIBLE);
        backArrowLayout.setClickable(true);

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

    @Override
    public void onPause(){
        super.onPause();

        backArrowLayout.setClickable(false);
        backArrowLayout.setVisibility(View.GONE);
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

        for(int n = 0; n < 1000; n++)
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
                PrintToSortedTeam(team1View, bestTeam1);
                PrintToSortedTeam(team2View, bestTeam2);
            }
        }

    }

    private void PrintToTeam(LinearLayout teamView, List<Friend> team) {
        teamView.removeAllViews();
        for (Friend f : team) {
            addPlayer(teamView, f);
        }
    }

    private void PrintToSortedTeam(LinearLayout teamView, List<Friend> team) {
        teamView.removeAllViews();
        team = sortFriendList(team);
        for (Friend f : team) {
            addPlayer(teamView, f);
        }
    }

    private List<Friend> sortFriendList(List<Friend> friendlist) {
        List<Friend> sortedlist = new ArrayList<>();
        int highestRank;
        Friend highestPlayer;
        int currentRank;
        while (friendlist.size() > 0) {
            highestRank = -1;
            highestPlayer = friendlist.get(0);
            for (Friend f : friendlist) {
                currentRank = f.getRank().ordinal();
                if (currentRank > highestRank) {
                    highestRank = currentRank;
                    highestPlayer = f;
                }
            }
            friendlist.remove(highestPlayer);
            sortedlist.add(highestPlayer);
        }
        return sortedlist;
    }


    /*
     * Creates a new child lay out with content representing FriendData
     * and ads it to the team LinearLayout.
     *
     * The xml would look like this
     *
        <LinearLayout
            android:id="@+id/root"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:orientation="horizontal">

            <LinearLayout
                android:id="@+id/card"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:background="@color/colorPrimaryTint"
                android:layout_marginBottom="0dp"
                android:layout_marginEnd="10dp"
                android:layout_marginStart="0dp"
                android:layout_marginTop="10dp"
                android:paddingLeft="5dp"
                android:paddingTop="5dp"
                android:paddingRight="5dp"
                android:paddingBottom="5dp">

                <LinearLayout
                    android:id="@+id/text"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical">

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

                <ImageView
                    android:id="@+id/rank_image"
                    android:src="@drawable/bronzei"
                    android:scaleType="fitXY"
                    android:layout_width="40dp"
                    android:layout_height="40dp"
                    android:layout_marginEnd="15dp"
                    android:layout_marginRight="15dp"
                    android:layout_marginStart="15dp"
                    android:layout_marginLeft="15dp"/>

            </LinearLayout>
        </LinearLayout>
     *
     */
    private void addPlayer(ViewGroup team, Friend friend) {
        LinearLayout.LayoutParams params;
        ConstraintLayout.LayoutParams paramsCL;

        LinearLayout root = new LinearLayout(getActivity());
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(params);

        {
            LinearLayout card = new LinearLayout(getActivity());
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            params.weight = 2;
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
            card.setLayoutParams(params);
            card.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint));
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setPadding(5, 10, 5, 20);

            {
                LinearLayout text = new LinearLayout(getActivity());
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                text.setLayoutParams(params);
                text.setOrientation(LinearLayout.VERTICAL);

                {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView username = new TextView(getActivity());
                    username.setLayoutParams(params);
                    username.setTypeface(friz);
                    int maxLength = 5;
//                    InputFilter[] fArray = new InputFilter[1];
//                    fArray[0] = new InputFilter.LengthFilter(maxLength);
//                    username.setFilters(fArray);
                    username.setEms(maxLength);
                    username.setMaxLines(2);
                    username.setText(friend.getUsername());




                    text.addView(username);
                }

                {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView rank = new TextView(getActivity());
                    rank.setLayoutParams(params);
                    rank.setTypeface(friz);
                    rank.setText(friend.getRank().toString());
                    rank.setTextColor(friend.getRankColor(getContext()));
                    rank.setMaxLines(1);

                    text.addView(rank);
                }

                card.addView(text);
            }
            {
                ConstraintLayout imageCL = new ConstraintLayout(getActivity());
                paramsCL = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                imageCL.setLayoutParams(paramsCL);

                {

                    ImageView rankImage = new ImageView(getActivity());
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMargins(5, 0, 5, 0);
                    params.weight = 1;
                    rankImage.setLayoutParams(params);
                    rankImage.setImageDrawable(friend.getRankGraphic(getActivity()));
                    //rankImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    //rankImage.setAdjustViewBounds(false);

                    ConstraintSet set = new ConstraintSet();
                    set.clone(imageCL);
                    set.setDimensionRatio(rankImage.getId(), "1:1");
                    set.applyTo(imageCL);


                    card.addView(rankImage);
                }

            }

            root.addView(card);
        }
        team.addView(root);
    }
}
