package com.example.benjamin.teambalancer;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.gui.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Friend> playerList;
    ConstraintLayout backarrowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backarrowLayout = findViewById(R.id.backarrow_layout);
        backarrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switchToMainPageFragment();

        // TODO: add in players from savedInstanceState
        playerList = new ArrayList<>();
    }

    public void setPlayerList(List<Friend> List) {
        playerList = List;
    }

    public List<Friend> getPlayerList() {
        return playerList;
    }

    public void switchToMainPageFragment() {
        Fragment fragment = new MainPageFragment();
        setFragment(fragment);
    }

    public void switchToFriendsFragment() {
        Fragment fragment = new FriendsFragment();
        setFragment(fragment);
    }

    public void switchToVersesFragment() {
        Fragment fragment = new VersesFragment();
        setFragment(fragment);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null).commit();
    }
}
