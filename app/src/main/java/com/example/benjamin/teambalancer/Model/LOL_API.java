package com.example.benjamin.teambalancer.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.benjamin.teambalancer.gui.FilterFriendRVAdapter;
import com.example.benjamin.teambalancer.gui.ISpinnerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class LOL_API {
    final private String APIKey = "RGAPI-ee9841e3-437d-429a-96bf-b962e011b31a";
    static LOL_API instance;
    Context context;

    private LOL_API(Context context) {
        this.context = context;
    }

    public static LOL_API getInstance(Context context) {
        if (instance == null) {
            instance = new LOL_API(context);
        }
        else {
            instance.context = context;
        }

        return instance;
    }

    public void addSummToPrefs(Friend Summoner) {
        FriendsList friendslist = FriendsList.getInstance(context);
        SharedPreferences prefs = context.getSharedPreferences(friendslist.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> canNullFriendSet = prefs.getStringSet(friendslist.PREF_SET_NAME, null);
        if (canNullFriendSet == null) {
            canNullFriendSet = new HashSet<>();
        }
        Set<String> FriendSet = new HashSet<>(canNullFriendSet);

        String username = Summoner.getUsername();
        String rank = Integer.toString(Summoner.getRank().ordinal());
        String SummString = username + friendslist.DELIMITER + rank;

        FriendSet.add(SummString);

        editor.putStringSet(friendslist.PREF_SET_NAME, FriendSet);
        editor.apply();

    }

    // Request a string response from the provided URL.
    public void getSummonerInfo(String inName, final FilterFriendRVAdapter adapter, final ISpinnerDialog dialog) {
        final String summonerName = inName.replaceAll("\\s+", "%20");

        // Instantiate the RequestQueue.
        RequestQueue queue1 = Volley.newRequestQueue(context);
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + APIKey;

        StringRequest summonerRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("API call", "onResponse: " + response);
                        try {
                            JSONObject respJSONObj = new JSONObject(response);
                            final String summonerID = respJSONObj.getString("id");
                            final String realSummonerName = respJSONObj.getString("name");
                            //int maxItems = result.getInt("end");
                            //JSONArray resultList = result.getJSONArray("item");
                            Log.d("API call", "Response is: " + response + summonerID);
                            // MAKE NEXT REQUEST.
                            RequestQueue queue2 = Volley.newRequestQueue(context);
                            String url = "https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/" + summonerID + "?api_key=" + APIKey;
                            // Request a string response from the provided URL.
                            StringRequest queueRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            Log.d("API call", "Response is: " + response);
                                            try {
                                                JSONArray queueArray = new JSONArray(response);
                                                JSONObject queueStat;
                                                String queueType, tier = "", div = "";
                                                LOLRank rank;
                                                for (int i = 0; i < queueArray.length(); i++) {
                                                    queueStat = queueArray.getJSONObject(i);
                                                    queueType = queueStat.getString("queueType");
                                                    if (queueType.equals("RANKED_SOLO_5x5")) {
                                                        tier = queueStat.getString("tier");
                                                        div = queueStat.getString("rank");
                                                    }
                                                }
                                                Friend summoner = new Friend(realSummonerName);
                                                if (tier != null && div != null) {
                                                    summoner.setRank(tier, div);
                                                }
                                                summoner.setSelected(true);
                                                addSummToPrefs(summoner);
                                                adapter.add(0, summoner);
                                                adapter.notifyDataSetChanged();
                                                //int maxItems = result.getInt("end");
                                                //JSONArray resultList = result.getJSONArray("item");
                                                Log.d("API call", "Response is: " + response + summonerID);
                                            } catch (JSONException e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            Log.d("API", "Both API calls succeeded!");
                                            dialog.showSuccess();

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("API call","That didn't work!", error);
                                    Toast.makeText(context, "That didn't work", Toast.LENGTH_SHORT);
                                    dialog.showFailure();
                                }
                            });

                            queue2.add(queueRequest);

                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            dialog.showFailure();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Summoner Name not found", Toast.LENGTH_SHORT).show();
                dialog.showFailure();
            }
        });

// Add the request to the RequestQueue
        queue1.add(summonerRequest);
    }

}
