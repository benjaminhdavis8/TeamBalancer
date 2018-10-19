package com.example.benjamin.teambalancer.gui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendsList;
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.List;

public class SelectFragment extends Fragment {

    private static final int MAX_PLAYERS = 10;
    private static final int MIN_PLAYERS = 2;
    FriendsRVAdapter adapter;
    FloatingActionButton balanceButton;
    ImageView backarrow;
    ConstraintLayout backArrowLayout;
    EditText searchBox;
    TextView selectedCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        RecyclerView rv = view.findViewById(R.id.friends_RV);
        LinearLayoutManager ll = new LinearLayoutManager(view.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        rv.setLayoutManager(ll);
        adapter = new FriendsRVAdapter();
        rv.setAdapter(adapter);

        searchBox = view.findViewById(R.id.search_bar);
        selectedCount = view.findViewById(R.id.selected_count);

        backarrow = getActivity().findViewById(R.id.backarrow);
        backArrowLayout = getActivity().findViewById(R.id.backarrow_layout);

        balanceButton = view.findViewById(R.id.action_button);
        balanceButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow_forward));
        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                activity.setPlayerList(adapter.getSelected());
                activity.switchToVersesFragment();
                }
            });
        setActionButtonVisible();

        EditText search = view.findViewById(R.id.search_bar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        backArrowLayout.setVisibility(View.VISIBLE);
        backArrowLayout.setClickable(true);
    }

    @Override
    public void onPause(){
        super.onPause();

        backArrowLayout.setVisibility(View.INVISIBLE);
        backArrowLayout.setClickable(false);
    }

    private void setActionButtonVisible() {
        if (adapter.NumSelected < MIN_PLAYERS || adapter.NumSelected > MAX_PLAYERS) {
            balanceButton.setVisibility(View.INVISIBLE);
        }
        else {
            balanceButton.setVisibility(View.VISIBLE);
        }
    }

    private class FriendsRVAdapter extends  RecyclerView.Adapter<FriendsRVAdapter.ViewHolder> {
        List<Friend> dataList;
        int NumSelected = 0;

        FriendsRVAdapter() {
            // debug constructor
            dataList = FriendsList.getInstance().getFriends();
            for (Friend f: dataList) {
                f.setSelected(false);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_data, parent, false);
            return new FriendsRVAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.Username.setText(dataList.get(position).getUsername());
            holder.Rank.setText(dataList.get(position).getRankText());
            holder.setIndex(position);
            holder.setBGColor();
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public List<Friend> getSelected() {
            List<Friend> ret = new ArrayList<>();
            for (Friend f : dataList) {
                if (f.getSelected()) {
                    ret.add(f);
                }
            }
            return ret;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final LinearLayout item;
            final TextView Username;
            final TextView Rank;
            final ImageView RankGraphic;
            int Index;
            //ImageView PersonalImage;
            //ImageView RankImage;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                RankGraphic = itemView.findViewById(R.id.rank_image);
                item = itemView.findViewById(R.id.friend_field);
                Index = -1;
            }

            void setBGColor() {
                boolean selected = dataList.get(Index).getSelected();
                if (selected) {
                    setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                } else {
                    setBackgroundColor(getContext().getResources().getColor(R.color.background));
                }
            }

            void setIndex(final int Index) {
                this.Index = Index;
                Username.setText(dataList.get(Index).getUsername());
                setRank(dataList.get(Index));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selected = dataList.get(Index).getSelected();
                        if (!selected) {
                            if (NumSelected < MAX_PLAYERS) {
                                setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                                NumSelected++;
                                selectedCount.setText(NumSelected + "/10");
                                dataList.get(Index).setSelected(true);
                            }
                            else {
                                Toast.makeText(getActivity(), "Too many players selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            setBackgroundColor(getContext().getResources().getColor(R.color.background));
                            NumSelected--;
                            selectedCount.setText(NumSelected + "/10");
                            dataList.get(Index).setSelected(false);
                        }

                        setActionButtonVisible();
                    }
                });
            }

            private void setRank(Friend friendData) {
                Rank.setText(friendData.getRankText());
                Rank.setTextColor(friendData.getRankColor(getContext()));

                LOLRank Enum = friendData.getRank();
                Drawable drawable = friendData.getRankGraphic(getActivity());
                RankGraphic.setImageDrawable(drawable);
            }

            void setBackgroundColor(int color) {
                item.setBackgroundColor(color);
            }
        }
    }
}
