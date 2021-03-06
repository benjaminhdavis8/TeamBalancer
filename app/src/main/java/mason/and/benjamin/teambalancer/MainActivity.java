package mason.and.benjamin.teambalancer;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mason.and.benjamin.teambalancer.Model.Friend;

import java.util.ArrayList;
import java.util.List;

import mason.and.benjamin.teambalancer.gui.FriendsFragment;
import mason.and.benjamin.teambalancer.gui.MainPageFragment;
import mason.and.benjamin.teambalancer.gui.SelectFragment;
import mason.and.benjamin.teambalancer.gui.VersesFragment;

public class MainActivity extends AppCompatActivity {
    List<Friend> playerList;
    List<Friend> friendList;
    ConstraintLayout backArrowLayout;
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backArrowLayout = findViewById(R.id.backarrow_layout);
        backArrowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fm = getSupportFragmentManager();

        addMainPageFragment();

        // TODO: add in players from savedInstanceState
        playerList = new ArrayList<>();
    }

    public void setPlayerList(List<Friend> List) {
        playerList = List;
    }

    public List<Friend> getPlayerList() {
        return playerList;
    }

    public void addMainPageFragment() {
        Fragment fragment = new MainPageFragment();
        fm.beginTransaction().add(R.id.main_fragment, fragment)
                .addToBackStack(null).commit();
    }

    public void switchToSelectFragment() {
        Fragment fragment = new SelectFragment();
        setFragment(fragment);
    }

    public void switchToFriendsFragment() {
        Fragment fragment = new FriendsFragment();
        setFragment(fragment);
    }

    public void switchToVersesFragment() {
        Fragment fragment = new VersesFragment();
//        setFragment(fragment);
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null).commit();

    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

        Object fragClass = fm.getFragments().get(0);
        if (fragClass instanceof MainPageFragment) {
            // Pretty sure default is to make app close, but I like
            // pressing back button and the app not closing.

            // finish();
        }
        else {
            super.onBackPressed();
        }
    }
}
