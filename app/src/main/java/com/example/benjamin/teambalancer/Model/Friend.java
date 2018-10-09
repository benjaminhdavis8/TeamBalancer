package com.example.benjamin.teambalancer.Model;

public class Friend {
    String Username;
    LOLRank Rank;

    public Friend(String Username) {
        this.Username = Username;
        Rank = LOLRank.Pending;
    }
}
