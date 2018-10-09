package com.example.benjamin.teambalancer.Model;


public class FriendData {
    public final String Username;
    public final String Rank;
    Boolean Selected;
    //Image personalImage
    //image rankImage

    FriendData(Freind sorce) {
        Username = sorce.Username;
        Rank = sorce.Rank.toString();
        Selected = false;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }


}
