package com.example.benjamin.teambalancer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.benjamin.teambalancer.gui.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchToFrendsFragmen();
    }

    public void switchToEntryFragment() {
        Fragment fragment = new EntryFragment();
        setFragment(fragment);
    }

    public void switchToFrendsFragmen() {
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
