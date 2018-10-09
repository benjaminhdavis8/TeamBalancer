package com.example.benjamin.teambalancer.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjamin.teambalancer.MainActivity;
import com.example.benjamin.teambalancer.Model.FriendData;
import com.example.benjamin.teambalancer.R;

import java.util.List;

public class FriendsFragment extends Fragment {

    private FriendsRVAdapter adapter;

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

        FloatingActionButton balance = view.findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).switchToVersesFragment();
            }
        });

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

        return view;
    }

    private class FriendsRVAdapter extends  RecyclerView.Adapter<FriendsRVAdapter.ViewHolder> {
        List<FriendData> dataList;

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
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final LinearLayout item;
            final TextView Username;
            final TextView Rank;
            int index;
            //ImageView PersonalImage;
            //ImageView RankImage;

            ViewHolder(View itemView) {
                super(itemView);
                Username = itemView.findViewById(R.id.username);
                Rank = itemView.findViewById(R.id.rank_string);
                item = itemView.findViewById(R.id.friend_field);
                index = -1;
            }

            void setIndex(final int Index) {
                this.index = Index;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean selected = dataList.get(Index).getSelected();
                        dataList.get(Index).setSelected(!selected);
                        setClickedColor(!selected);
                    }
                });
            }

            void setClickedColor(boolean clicked) {
                if (clicked) {
                    item.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryTint2));
                }
                else {
                    item.setBackgroundColor(getContext().getResources().getColor(R.color.background));
                }
            }
        }
    }
}
