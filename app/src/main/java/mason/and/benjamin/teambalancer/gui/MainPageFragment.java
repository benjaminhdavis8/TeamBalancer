package mason.and.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mason.and.benjamin.teambalancer.MainActivity;
import mason.and.benjamin.teambalancer.Model.Friend;
import mason.and.benjamin.teambalancer.Model.FriendsList;
import mason.and.benjamin.teambalancer.R;

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
