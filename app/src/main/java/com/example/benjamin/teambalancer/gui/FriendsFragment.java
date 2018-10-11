package com.example.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendData;
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    FriendsRVAdapter adapter;
    FloatingActionButton balanceButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        RecyclerView rv = view.findViewById(R.id.freinds_RV);
        LinearLayoutManager ll = new LinearLayoutManager(view.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        rv.setLayoutManager(ll);
        adapter = new FriendsRVAdapter();
        rv.setAdapter(adapter);

        balanceButton = view.findViewById(R.id.balance);
        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)getActivity();
                activity.setPlayerList(adapter.getSelected());
                activity.switchToVersesFragment();
            }
        });
        setActionButtonVisable();

        AppCompatImageButton AddButton = view.findViewById(R.id.add_button);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast message = new Toast(getActivity());
                message.setDuration(Toast.LENGTH_SHORT);
                message.setText("Not implemented");
                message.show();
            }
        });

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

    private void setActionButtonVisable() {
        if (adapter.NumSelected > 1) {
            balanceButton.setVisibility(View.VISIBLE);
        }
        else {
            balanceButton.setVisibility(View.INVISIBLE);
        }
    }

    private class FriendsRVAdapter extends  RecyclerView.Adapter<FriendsRVAdapter.ViewHolder> {
        private static final int MAX_PLAYERS = 10;
        List<FriendData> dataList;
        int NumSelected = 0;

        FriendsRVAdapter() {
            // debug constructor
            dataList = new ArrayList<>();
            dataList.add(new FriendData(new Friend("Faker", LOLRank.Master)));
            dataList.add(new FriendData(new Friend("Shwartz", LOLRank.Silver1)));
            dataList.add(new FriendData(new Friend("US Economy", LOLRank.Diamond4)));
            dataList.add(new FriendData(new Friend("Phoenix", LOLRank.Bronze5)));
            dataList.add(new FriendData(new Friend("Olock", LOLRank.Challenger)));
            dataList.add(new FriendData(new Friend("ddog", LOLRank.Diamond1)));
            dataList.add(new FriendData(new Friend("Cheeser",LOLRank.Platinum2)));
            dataList.add(new FriendData(new Friend("Aragon", LOLRank.Bronze1)));
            dataList.add(new FriendData(new Friend("Distruction", LOLRank.Bronze2)));
            dataList.add(new FriendData(new Friend("Trident")));
            dataList.add(new FriendData(new Friend("Belthezar", LOLRank.Bronze3)));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_data, parent, false);
            return new FriendsRVAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.Username.setText(dataList.get(position).Username);
            holder.Rank.setText(dataList.get(position).Rank);
            holder.setIndex(position);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public List<Friend> getSelected() {
            List<Friend> ret = new ArrayList<>();
            for (FriendData f : dataList) {
                if (f.getSelected()) {
                    ret.add(f.getOriginal());
                }
            }
            return ret;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final LinearLayout item;
            final TextView Username;
            final TextView Rank;
            int Index;
            //ImageView PersonalImage;
            //ImageView RankImage;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                item = itemView.findViewById(R.id.friend_field);
                Index = -1;
            }

            void setIndex(final int Index) {
                this.Index = Index;
                Username.setText(dataList.get(Index).Username);
                setRank(dataList.get(Index));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selected = !dataList.get(Index).getSelected();
                        if (selected) {
                            if (NumSelected < MAX_PLAYERS) {
                                setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                                NumSelected++;
                                dataList.get(Index).setSelected(true);
                            }
                            else {
                                Toast toast = new Toast(getContext());
                                toast.setText("Too many players selected");
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        else {
                            setBackgroundColor(getContext().getResources().getColor(R.color.background));
                            NumSelected--;
                            dataList.get(Index).setSelected(false);
                        }

                        setActionButtonVisable();
                    }
                });
            }

            private void setRank(FriendData friendData) {
                Rank.setText(friendData.Rank);
                Rank.setTextColor(friendData.RankColor);
            }

            void setBackgroundColor(int color) {
                item.setBackgroundColor(color);
            }
        }
    }
}
