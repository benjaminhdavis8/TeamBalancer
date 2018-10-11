package com.example.benjamin.teambalancer.Model;

import android.support.annotation.NonNull;

import static android.graphics.Color.BLACK;

public class Friend implements Comparable<Friend> {
    private final int BRONZE = 0xff783f04;
    private final int SILVER = 0xffcccccc;
    private final int GOLD = 0xffffd966;
    private final int PLATINUM = 0xffcccb94;
    private final int DIAMOND = 0xffeeeeee;
    private final int CHALLENGER = 0xff1155cc;
    private final int MASTER = 0xffcc7f00;

    private final int BASE_MRR = 850;

    private String Username;
    private LOLRank Rank;
    private Integer color;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getMMR() {
        int MMR = BASE_MRR + (60 * (Rank.ordinal() - 1));
        return MMR;
    }

    public LOLRank getRank() {
        return Rank;
    }

    public void setRank(LOLRank rank) {
        Rank = rank;
    }

    public Friend(String Username) {
        this.Username = Username;
        Rank = LOLRank.UNRANKED;
    }

    public Friend(String Username, LOLRank Rank) {
        this.Username = Username;
        this.Rank = Rank;
    }

    public int getRankColor() {
        if (color != null)
            return color;

        if (Rank == LOLRank.Bronze1 ||
            Rank == LOLRank.Bronze2 ||
            Rank == LOLRank.Bronze3 ||
            Rank == LOLRank.Bronze4 ||
            Rank == LOLRank.Bronze5) {
            color = BRONZE;
        }
        else if(Rank == LOLRank.Silver1 ||
                Rank == LOLRank.Silver2 ||
                Rank == LOLRank.Silver3 ||
                Rank == LOLRank.Silver4 ||
                Rank == LOLRank.Silver5) {
            color = SILVER;
        }
        else if(Rank == LOLRank.Gold1 ||
                Rank == LOLRank.Gold2 ||
                Rank == LOLRank.Gold3 ||
                Rank == LOLRank.Gold4 ||
                Rank == LOLRank.Gold5) {
            color = GOLD;
        }
        else if(Rank == LOLRank.Diamond1 ||
                Rank == LOLRank.Diamond2 ||
                Rank == LOLRank.Diamond3 ||
                Rank == LOLRank.Diamond4 ||
                Rank == LOLRank.Diamond5) {
            color = DIAMOND;
        }
        else if(Rank == LOLRank.Platinum1 ||
                Rank == LOLRank.Platinum2 ||
                Rank == LOLRank.Platinum3 ||
                Rank == LOLRank.Platinum4 ||
                Rank == LOLRank.Platinum5) {
            color = PLATINUM;
        }
        else if (Rank == LOLRank.Challenger) {
            color = CHALLENGER;
        }
        else if (Rank == LOLRank.Master) {
            color = MASTER;
        }
        else {
            color = BLACK;
        }
        return color;
    }

    @Override
    public int compareTo(@NonNull Friend o) {
        if (this.Rank.betterThan(o.Rank)) {
            return 1;
        }
        else if (o.Rank.betterThan(this.Rank)) {
            return -1;
        }
        return 0;
    }
}
