package com.example.benjamin.teambalancer.gui;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private static final int MAX_PLAYERS = 10;
    private static final int MIN_PLAYERS = 2;
    FriendsRVAdapter adapter;
    FloatingActionButton addButton;
    ImageView backArrow;
    ConstraintLayout backArrowLayout;
    EditText searchBox;

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

        backArrow = getActivity().findViewById(R.id.backarrow);
        backArrowLayout = getActivity().findViewById(R.id.backarrow_layout);

        addButton = view.findViewById(R.id.action_button);
        addButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add));
        addButton.setVisibility(View.VISIBLE);
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
                        if(edit.getText().length() > 0) {
                            adapter.add(0, new Friend(edit.getText().toString()));
                            adapter.notifyDataSetChanged();
                            edit.setText("");
                        }
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
                adapter.setFilterString(s.toString());
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


//**Adapter****************************************************************************************

    private class FriendsRVAdapter extends  FilterFriendRVAdapter<FriendsRVAdapter.ViewHolder> {
        int NumSelected = 0;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_data, parent, false);
            return new FriendsRVAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.Username.setText(filteredList.get(position).getUsername());
            holder.Rank.setText(filteredList.get(position).getRankText());
            holder.setIndex(position);
        }

        public List<Friend> getSelected() {
            List<Friend> ret = new ArrayList<>();
            setFilterString("");
            for (Friend f : filteredList) {
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
            final Button deleteButton;
            int Index;
            //ImageView PersonalImage;
            //ImageView RankImage;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                item = itemView.findViewById(R.id.friend_field);
                RankGraphic = itemView.findViewById(R.id.rank_image);
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
                                remove(Index);
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
                Username.setText(filteredList.get(Index).getUsername());
                setRank(filteredList.get(Index));
            }

            private void setRank(Friend friend) {
                Rank.setText(friend.getRankText());

                Rank.setTextColor(friend.getRankColor(getContext()));
                LOLRank Enum = friend.getRank();
                Drawable drawable = friend.getRankGraphic(getActivity());
                RankGraphic.setImageDrawable(drawable);
            }

        }
    }
}
