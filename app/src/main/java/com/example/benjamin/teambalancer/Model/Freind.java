package com.example.benjamin.teambalancer.Model;

public class Freind {
    String Username;
    LOLRank Rank;

    Freind(String Username) {
        this.Username = Username;
        Rank = LOLRank.Pending;
    }
}
