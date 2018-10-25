package com.example.benjamin.teambalancer.gui;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.LOLRank;
import com.example.benjamin.teambalancer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    final String APIKey = "RGAPI-4dae66e7-fa7f-4c31-beb0-56b21a35d3d9";

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
            final Dialog cardDialog = new Dialog(getActivity());
            final Dialog afterDialog = new Dialog(getActivity());
            String summonerName;
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.add_friend_dialog);
                cardDialog.setContentView(R.layout.add_friend_card_dialog);
                afterDialog.setContentView(R.layout.add_friend_after_dialog);

                final EditText edit = dialog.findViewById(R.id.edit);
                edit.setText(searchBox.getText());

                final EditText editAfter = afterDialog.findViewById(R.id.edit);

                final Button cancel = afterDialog.findViewById(R.id.cancel_button);
                final Button addAfter = afterDialog.findViewById(R.id.save_button);
                afterDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        addAfter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(editAfter.getText().length() > 0) {
//                                    adapter.add(0, new Friend(edit.getText().toString()));
//                                    adapter.notifyDataSetChanged();
                                    summonerName = editAfter.getText().toString();
                                    summonerName = summonerName.replaceAll("\\s+", "%20");
                                    editAfter.setText("");
                                    afterDialog.cancel();
                                    cardDialog.show();
                                }
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                afterDialog.cancel();
                            }
                        });
                    }
                });

                final TextView cardText = (TextView) cardDialog.findViewById(R.id.hint);
                cardDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                        // Instantiate the RequestQueue.
                        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
                        String url ="https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + APIKey;

                        // Request a string response from the provided URL.
                        StringRequest summonerRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        cardText.setText("Response is: "+ response);
                                        try {
                                            JSONObject respJSONObj = new JSONObject(response);
                                            final String summonerID = respJSONObj.getString("id");
                                            summonerName = respJSONObj.getString("name");
                                            //int maxItems = result.getInt("end");
                                            //JSONArray resultList = result.getJSONArray("item");
                                            cardText.setText("Response is: "+ response + summonerID);
                                            // MAKE NEXT REQUEST.
                                            RequestQueue queue2 = Volley.newRequestQueue(getActivity());
                                            String url ="https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/" + summonerID + "?api_key=" + APIKey;
                                            // Request a string response from the provided URL.
                                            StringRequest queueRequest = new StringRequest(Request.Method.GET, url,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            // Display the first 500 characters of the response string.
                                                            cardText.setText("Response is: "+ response);
                                                            try {
                                                                JSONArray queueArray = new JSONArray(response);
                                                                JSONObject queueStat;
                                                                String queueType, tier="", div="";
                                                                LOLRank rank;
                                                                for (int i=0; i<queueArray.length(); i++)
                                                                {
                                                                    queueStat = queueArray.getJSONObject(i);
                                                                    queueType = queueStat.getString("queueType");
                                                                    if (queueType.equals("RANKED_SOLO_5x5"))
                                                                    {
                                                                        tier = queueStat.getString("tier");
                                                                        div = queueStat.getString("rank");
                                                                    }
                                                                }
                                                                Friend summoner = new Friend(summonerName);
                                                                if (tier != null && div != null) {
                                                                    summoner.setRank(tier, div);
                                                                }
                                                                adapter.add(0, summoner);
                                                                adapter.notifyDataSetChanged();
                                                                summonerName = edit.getText().toString();
                                                                //int maxItems = result.getInt("end");
                                                                //JSONArray resultList = result.getJSONArray("item");
                                                                cardText.setText("Response is: "+ response + summonerID);
                                                            } catch (JSONException e) {
                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                                cardDialog.cancel();
                                                                afterDialog.show();
                                                            }

                                                            Log.d("API", "Both API calls succeeded!");
                                                            cardDialog.cancel();
                                                            afterDialog.show();

                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    cardText.setText("That didn't work!");
                                                    cardDialog.cancel();
                                                    afterDialog.show();
                                                }
                                            });

                                            queue2.add(queueRequest);

                                        } catch (JSONException e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            cardDialog.cancel();
                                            afterDialog.show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                cardText.setText("That didn't work!");
                                cardDialog.cancel();
                                afterDialog.show();
                            }
                        });

// Add the request to the RequestQueue.
                        queue1.add(summonerRequest);

                    }
                });

                Button add = dialog.findViewById(R.id.save_button);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit.getText().length() > 0) {
//                            adapter.add(0, new Friend(edit.getText().toString()));
//                            adapter.notifyDataSetChanged();
                            summonerName = edit.getText().toString();
                            summonerName = summonerName.replaceAll("\\s+", "%20");
                            edit.setText("");
                            dialog.cancel();
                            cardDialog.show();
                        }
                    }
                });


                dialog.show();
//                cardDialog.show();
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
