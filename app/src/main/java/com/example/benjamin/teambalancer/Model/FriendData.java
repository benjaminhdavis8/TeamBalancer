package com.example.benjamin.teambalancer.Model;


public class FriendData {
    private final Freind RealOne;
    public final String Username;
    public final String Rank;
    Boolean Selected;
    //Image personalImage
    //image rankImage

    FriendData(Freind source) {
        RealOne = source;
        Username = source.Username;
        Rank = source.Rank.toString();
        Selected = false;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }


}
