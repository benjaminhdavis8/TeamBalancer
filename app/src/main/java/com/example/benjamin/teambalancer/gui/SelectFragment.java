package com.example.benjamin.teambalancer.gui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.Model.LOL_API;
import com.example.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.List;

public class SelectFragment extends Fragment {

    private static final int MAX_PLAYERS = 10;
    private static final int MIN_PLAYERS = 2;
    FriendsRVAdapter adapter;
    private ru.dimorinny.floatingtextbutton.FloatingTextButton balanceButton;
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

        searchBox = view.findViewById(R.id.search_bar);
        selectedCount = view.findViewById(R.id.selected_count);

        adapter = new FriendsRVAdapter();
        rv.setAdapter(adapter);

        backarrow = getActivity().findViewById(R.id.backarrow);
        backArrowLayout = getActivity().findViewById(R.id.backarrow_layout);

        balanceButton = view.findViewById(R.id.action_button);
//        balanceButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow_forward));
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

    private void setActionButtonVisible() {
        if (adapter.NumSelected < MIN_PLAYERS || adapter.NumSelected > MAX_PLAYERS) {
            balanceButton.setVisibility(View.INVISIBLE);
        }
        else {
            balanceButton.setVisibility(View.VISIBLE);
        }
    }


    //**Adapter****************************************************************************************

    private class FriendsRVAdapter extends FilterFriendRVAdapter<FriendsRVAdapter.ViewHolder> {
        int NumSelected = 0;

        FriendsRVAdapter() {
            // debug constructor
            super();
            for (Friend f: filteredList) {
                //f.setSelected(false);
                if (f.getSelected())
                {
                    NumSelected++;
                }
            }
            selectedCount.setText(NumSelected + "/10");

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
            holder.setIndex(position);
        }

        @Override
        public void NotifyDataSetChanged() {
            if (!addable.getUsername().isEmpty()) {
                filteredList.add(addable);
            }
            else if (filteredList.contains(addable)) {
                filteredList.remove(addable);
            }
            super.NotifyDataSetChanged();
        }

        public List<Friend> getSelected() {
            List<Friend> ret = new ArrayList<>();
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
            final View progressBar;
            //ImageView PersonalImage;
            final ImageView RankGraphic;
            int Index;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                RankGraphic = itemView.findViewById(R.id.rank_image);
                progressBar = itemView.findViewById(R.id.progressSpinner);
                item = itemView.findViewById(R.id.friend_field);
                Index = -1;
            }

            void setBGColor() {
                boolean selected = filteredList.get(Index).getSelected();
                if (selected) {
                    setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                } else {
                    setBackgroundColor(getContext().getResources().getColor(R.color.background));
                }
            }

            void setIndex(final int Index) {
                this.Index = Index;
                Username.setText(filteredList.get(Index).getUsername());

                if (filteredList.get(Index).equals(addable)) {
                    setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    Rank.setVisibility(View.INVISIBLE);
                    RankGraphic.setVisibility(View.INVISIBLE);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String username = addable.getUsername();
                            addable.setUsername(username.substring(4, username.length()-1));

                            LOL_API api = LOL_API.getInstance(getContext());
                            api.getSummonerInfo(addable.getUsername(), adapter, new ISpinnerDialog() {
                                @Override
                                public void showSpinner() {
                                    RankGraphic.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void showSuccess() {
                                    searchBox.setText("");
                                    clearFilterString();
                                    progressBar.setVisibility(View.GONE);
                                    RankGraphic.setVisibility(View.VISIBLE);
                                    Rank.setVisibility(View.VISIBLE);
                                    setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                                    setBGColor();
                                    if (filteredList.contains(addable)) {
                                        filteredList.remove(addable);
                                    }
                                    if (NumSelected < MAX_PLAYERS) {
                                        filteredList.get(0).setSelected(true);
                                        NumSelected++;
                                        selectedCount.setText(NumSelected + "/10");
                                        NotifyDataSetChanged();
                                    }
                                    addable = new Friend("");
                                }

                                @Override
                                public void showFailure() {
                                    progressBar.setVisibility(View.GONE);
                                    setBackgroundColor(getContext().getResources().getColor(R.color.GOLD));
                                    final Runnable restore = new Runnable() {
                                        @Override
                                        public void run() {
                                            setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                                        }
                                    };

                                    new Thread(restore) {
                                        public void run() {
                                            try {
                                                Thread.sleep(300);
                                                addable = new Friend("");
                                                getActivity().runOnUiThread(restore);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                }
                            });
                        }
                    });
                    return;
                }

                setRank(filteredList.get(Index));
                setBGColor();
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selected = filteredList.get(Index).getSelected();
                        if (!selected) {
                            if (NumSelected < MAX_PLAYERS) {
                                setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                                NumSelected++;
                                selectedCount.setText(NumSelected + "/10");
                                filteredList.get(Index).setSelected(true);
                            }
                            else {
                                Toast.makeText(getActivity(), "Too many players selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            setBackgroundColor(getContext().getResources().getColor(R.color.background));
                            NumSelected--;
                            selectedCount.setText(NumSelected + "/10");
                            filteredList.get(Index).setSelected(false);
                        }

                        setActionButtonVisible();
                    }
                });
            }

            private void setRank(Friend friendData) {
                Rank.setVisibility(View.VISIBLE);
                Rank.setText(friendData.getRankText());
                Rank.setTextColor(friendData.getRankColor(getContext()));

                LOLRank Enum = friendData.getRank();
                Drawable drawable = friendData.getRankGraphic(getActivity());
                RankGraphic.setVisibility(View.VISIBLE);
                RankGraphic.setImageDrawable(drawable);
            }

            void setBackgroundColor(int color) {
                item.setBackgroundColor(color);
            }
        }
    }
}
