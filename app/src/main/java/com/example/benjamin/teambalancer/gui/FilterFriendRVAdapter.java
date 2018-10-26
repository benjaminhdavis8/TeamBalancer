package com.example.benjamin.teambalancer.gui;

import android.support.v7.widget.RecyclerView;

import com.example.benjamin.teambalancer.Model.Friend;
import com.example.benjamin.teambalancer.Model.FriendsList;

import java.util.ArrayList;
import java.util.List;

abstract public class FilterFriendRVAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private List<Friend> dataList;
    List<Friend> filteredList;
    String filterString;
    public Friend addable;

    FilterFriendRVAdapter() {
        // debug constructor
        dataList = FriendsList.getInstance().getFriends();
        filteredList = new ArrayList<>();
        filterString = "";
        addable = new Friend("");
        filter();
    }

    public void setFilterString(String s) {
        this.filterString = s.toLowerCase();
        if (s.equals("")) {
            addable.setUsername("");
        }
        else {
            addable.setUsername("add " + filterString + "?");
        }
        filter();
    }

    public String getFilterString() {
        return filterString;
    }

    public void clearFilterString() {
        this.filterString = "";
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private void filter() {
       filteredList.clear();
        for (Friend f :
                dataList) {
            if(f.getUsername().toLowerCase().contains(filterString)) {
                filteredList.add(f);
            }
        }
        NotifyDataSetChanged();
    }

    public void NotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    public void remove(int i) {
        dataList.remove(i);
        filter();
    }

    public void add(int index, Friend friend) {
        dataList.add(index, friend);
        filter();
    }

}
