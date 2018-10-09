package com.example.benjamin.teambalancer.Model;


public class FriendData {
    private final Friend RealOne;
    public final String Username;
    public final String Rank;
    Boolean Selected;
    //Image personalImage
    //image rankImage

    public FriendData(Friend source) {
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
