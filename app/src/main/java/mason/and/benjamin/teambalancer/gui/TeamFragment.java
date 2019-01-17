package mason.and.benjamin.teambalancer.gui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
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
import android.widget.Toast;

import mason.and.benjamin.teambalancer.MainActivity;
import mason.and.benjamin.teambalancer.Model.Friend;
import mason.and.benjamin.teambalancer.Model.LOLRank;
import mason.and.benjamin.teambalancer.R;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment {

    private static final int MAX_PLAYERS = 10;
    FriendsRVAdapter adapter;
    FloatingActionButton balanceButton;
    ImageView backarrow;
    ConstraintLayout backarrowLayout;
    EditText searchBox;
    AppCompatImageButton addButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        searchBox = view.findViewById(R.id.search_bar);
        RecyclerView rv = view.findViewById(R.id.friends_RV);
        LinearLayoutManager ll = new LinearLayoutManager(view.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        rv.setLayoutManager(ll);
        adapter = new FriendsRVAdapter();
        rv.setAdapter(adapter);

        backarrow = getActivity().findViewById(R.id.backarrow);
        backarrowLayout = getActivity().findViewById(R.id.backarrow_layout);

        balanceButton = view.findViewById(R.id.action_button);
        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        backarrowLayout.setVisibility(View.VISIBLE);
        backarrowLayout.setClickable(true);
    }

    @Override
    public void onPause(){
        super.onPause();

        backarrowLayout.setVisibility(View.INVISIBLE);
        backarrowLayout.setClickable(false);
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
        List<Friend> dataList;
        int NumSelected = 0;

        FriendsRVAdapter() {
            // debug constructor
            dataList = new ArrayList<>();
            dataList.add(new Friend("Faker", LOLRank.Master));
            dataList.add(new Friend("Shwartz", LOLRank.Silver1));
            dataList.add(new Friend("US Economy", LOLRank.Diamond4));
            dataList.add(new Friend("Phoenix", LOLRank.Bronze5));
            dataList.add(new Friend("Olock", LOLRank.Challenger));
            dataList.add(new Friend("ddog", LOLRank.Diamond1));
            dataList.add(new Friend("Cheeser",LOLRank.Platinum2));
            dataList.add(new Friend("Aragon", LOLRank.Bronze1));
            dataList.add(new Friend("Destruction", LOLRank.Bronze2));
            dataList.add(new Friend("Trident"));
            dataList.add(new Friend("Belthazar", LOLRank.Bronze3));
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
            holder.Username.setText(dataList.get(position).getUsername());
            holder.Rank.setText(dataList.get(position).getRank().toString());
            holder.setIndex(position);
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
                Username.setText(dataList.get(Index).getUsername());
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

            private void setRank(Friend friend) {
                Rank.setText(friend.getRank().toString());
                Rank.setTextColor(friend.getRankColor(getContext()));
            }

            void setBackgroundColor(int color) {
                item.setBackgroundColor(color);
            }
        }
    }
}

