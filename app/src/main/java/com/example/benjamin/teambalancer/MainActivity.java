package com.example.benjamin.teambalancer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.gui.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Friend> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchToFriendsFragment();
    }

    public void setPlayerList(List<Friend> List) {
        playerList = List;
    }

    public List<Friend> getPlayerList() {
        return playerList;
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
