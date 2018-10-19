package com.example.benjamin.teambalancer.Model;


import android.content.Context;
import android.support.annotation.NonNull;

public class FriendData implements Comparable<FriendData> {
    private final Friend RealOne;
    public final String Username;
    public final String Rank;
    public final int RankColor;
    Boolean Selected;
    //Image personalImage
    //image rankImage

    public FriendData(Friend source, Context context) {
        RealOne = source;
        Username = source.getUsername();
        Rank = source.getRank().toString();
        RankColor = source.getRankColor(context);

        Selected = false;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    public Friend getOriginal() {
        return RealOne;
    }

    @Override
    public int compareTo(@NonNull FriendData o) {
        return this.RealOne.compareTo(o.RealOne);
    }
}
