package com.example.benjamin.teambalancer.gui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.Model.LOL_API;
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
            final SpinnerDialog dialog = new SpinnerDialog(getActivity());
            String summonerName;
            @Override
            public void onClick(View v) {
                dialog.setContentView();

                final EditText edit = dialog.findViewById(R.id.edit);
                edit.setText(searchBox.getText());

                final Button cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Button add = dialog.findViewById(R.id.save_button);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = getActivity();
                        if (edit.getText().length() <= 0) {
                            return;
                        }

                        summonerName = edit.getText().toString();
                        summonerName = summonerName.replaceAll("\\s+", "%20");
                        edit.setText("");
                        cancel.setVisibility(View.VISIBLE);
                        searchBox.setText("");
                        edit.setText("");
                        dialog.showSpinner();

                        LOL_API api = LOL_API.getInstance(getContext());

                        api.getSummnerInfo(summonerName, adapter, dialog);
                    }


//                            final Runnable restore = new Runnable() {
//                                @Override
//                                public void run() {
//                                    forum.setVisibility(View.VISIBLE);
//                                    spinner.setVisibility(View.GONE);
//                                }
//                            };
//
//                            new Thread(restore) {
//                                public void run() {
//                                    try {
//                                        Thread.sleep(600); //TODO: replace this with api calls
//                                        activity.runOnUiThread(restore);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }.start();
//                            }

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

        searchBox.clearFocus();

        backArrowLayout.setClickable(false);
        backArrowLayout.setVisibility(View.GONE);
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
                    Dialog confirmDialog = new Dialog(getActivity());
                    @Override
                    public void onClick(View v) {
                        if (Index < 0) {
                            return;
                        }
                        confirmDialog.setContentView(R.layout.confirm_dialog);
                        TextView hint = confirmDialog.findViewById(R.id.confirmTitle);
                        hint.setText(hint.getText().toString().replace(getResources().getString(R.string.replaceable), Username.getText().toString()));

                        Button cancel = confirmDialog.findViewById(R.id.confirm_no);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.cancel();
                            }
                        });

                        Button delete = confirmDialog.findViewById(R.id.confirm_yes);
                            delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remove(Index);
                                notifyDataSetChanged();
                                confirmDialog.cancel();
                            }
                        });

                        confirmDialog.show();
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

    private class SpinnerDialog extends Dialog implements ISpinnerDialog {
        private View forumLayout;
        private View feedbackLayout;
        private View spinner;
        private View success;
        private View failure;

        public SpinnerDialog(FragmentActivity activity) {
            super(activity);
        }

        public void setContentView() {
            super.setContentView(R.layout.add_friend_dialog);

            forumLayout = findViewById(R.id.add_friend_forum);
            feedbackLayout = findViewById(R.id.progressBarLayout);
            spinner = findViewById(R.id.progressSpinner);
            success = findViewById(R.id.successImg);
            failure = findViewById(R.id.failImg);
        }

        @Override
        public void showSpinner() {
            forumLayout.setVisibility(View.GONE);
            feedbackLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            success.setVisibility(View.GONE);
            failure.setVisibility(View.GONE);
        }

        @Override
        public void showSuccess() {
            forumLayout.setVisibility(View.GONE);
            feedbackLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            success.setVisibility(View.VISIBLE);
            failure.setVisibility(View.GONE);
                final Runnable restore = new Runnable() {
                    @Override
                    public void run() {
                        feedbackLayout.setVisibility(View.GONE);
                        forumLayout.setVisibility(View.VISIBLE);
                    }
                };

                new Thread(restore) {
                    public void run() {
                        try {
                            Thread.sleep(300);
                            getActivity().runOnUiThread(restore);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
        }

        @Override
        public void showFailure() {
            forumLayout.setVisibility(View.GONE);
            feedbackLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            success.setVisibility(View.GONE);
            failure.setVisibility(View.VISIBLE);
            final Runnable restore = new Runnable() {
                @Override
                public void run() {
                    feedbackLayout.setVisibility(View.GONE);
                    forumLayout.setVisibility(View.VISIBLE);
                }
            };

            new Thread(restore) {
                public void run() {
                    try {
                        Thread.sleep(300);
                        getActivity().runOnUiThread(restore);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
