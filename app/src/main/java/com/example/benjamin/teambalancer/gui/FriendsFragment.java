package com.example.benjamin.teambalancer.gui;

import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private static final int MAX_PLAYERS = 10;
    FriendsRVAdapter adapter;
    FloatingActionButton balanceButton;
    EditText searchBox;
    AppCompatImageButton addButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        searchBox = view.findViewById(R.id.search_bar);
        RecyclerView rv = view.findViewById(R.id.friends_RV);
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
        setActionButtonVisible();

        addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            final Dialog dialog = new Dialog(getActivity());
            @Override
            public void onClick(View v) {
                    dialog.setContentView(R.layout.add_friend_dialog);
                    final EditText edit = dialog.findViewById(R.id.edit);
                    edit.setText(searchBox.getText());

                    Button cancel = dialog.findViewById(R.id.cancel_button);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    Button save = dialog.findViewById(R.id.save_button);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           adapter.dataList.add(new FriendData(new Friend(edit.getText().toString())));
                           adapter.notifyDataSetChanged();
                           dialog.cancel();
                        }
                    });

                    dialog.show();
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

    private void setActionButtonVisible() {
        if (adapter.NumSelected == MAX_PLAYERS) {
            balanceButton.setVisibility(View.VISIBLE);
        }
        else {
            balanceButton.setVisibility(View.INVISIBLE);
        }
    }

    private class FriendsRVAdapter extends  RecyclerView.Adapter<FriendsRVAdapter.ViewHolder> {
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
            dataList.add(new FriendData(new Friend("Destruction", LOLRank.Bronze2)));
            dataList.add(new FriendData(new Friend("Trident")));
            dataList.add(new FriendData(new Friend("Belthazar", LOLRank.Bronze3)));
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
            final ImageButton deleteButton;
            int Index;
            //ImageView PersonalImage;
            //ImageView RankImage;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                item = itemView.findViewById(R.id.friend_field);
                deleteButton = itemView.findViewById(R.id.delete_button);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    Dialog dialog = new Dialog(getActivity());
                    @Override
                    public void onClick(View v) {
                        if (Index < 0) {
                            return;
                        }
                        dialog.setContentView(R.layout.confirm_dialog);
                        TextView hint = dialog.findViewById(R.id.confirmTitle);
                        hint.setText(hint.getText().toString().replace(getResources().getString(R.string.replaceable), Username.getText().toString()));

                        Button cancel = dialog.findViewById(R.id.confirm_no);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        Button delete = dialog.findViewById(R.id.confirm_yes);
                            delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataList.remove(Index);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });

                        dialog.show();
                    }
                });
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
                                Toast.makeText(getActivity(), "Too many players selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            setBackgroundColor(getContext().getResources().getColor(R.color.background));
                            NumSelected--;
                            dataList.get(Index).setSelected(false);
                        }

                        setActionButtonVisible();
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
