package com.example.benjamin.teambalancer.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.example.benjamin.teambalancer.R;

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
    private Drawable drawable;
    private Integer color;
    private
    Boolean Selected;

    public Friend(String Username) {
        this.Username = Username;
        Rank = LOLRank.UNRANKED;
        Selected = false;
    }

    public Friend(String Username, LOLRank Rank) {
        this.Username = Username;
        this.Rank = Rank;
        Selected = false;
    }

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

    public String getRankText() {
        return this.getRank().toString();
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    public int getRankColor(Context context) {

        if (color != null)
            return color;

        if (Rank == LOLRank.Bronze1 ||
            Rank == LOLRank.Bronze2 ||
            Rank == LOLRank.Bronze3 ||
            Rank == LOLRank.Bronze4 ||
            Rank == LOLRank.Bronze5) {
            color = BRONZE;
            color = ContextCompat.getColor(context, R.color.BRONZE);
        }
        else if(Rank == LOLRank.Silver1 ||
                Rank == LOLRank.Silver2 ||
                Rank == LOLRank.Silver3 ||
                Rank == LOLRank.Silver4 ||
                Rank == LOLRank.Silver5) {
            color = SILVER;
            color = ContextCompat.getColor(context, R.color.SILVER);
        }
        else if(Rank == LOLRank.Gold1 ||
                Rank == LOLRank.Gold2 ||
                Rank == LOLRank.Gold3 ||
                Rank == LOLRank.Gold4 ||
                Rank == LOLRank.Gold5) {
            color = GOLD;
            color = ContextCompat.getColor(context, R.color.GOLD);
        }
        else if(Rank == LOLRank.Diamond1 ||
                Rank == LOLRank.Diamond2 ||
                Rank == LOLRank.Diamond3 ||
                Rank == LOLRank.Diamond4 ||
                Rank == LOLRank.Diamond5) {
            color = DIAMOND;
            color = ContextCompat.getColor(context, R.color.DIAMOND);
        }
        else if(Rank == LOLRank.Platinum1 ||
                Rank == LOLRank.Platinum2 ||
                Rank == LOLRank.Platinum3 ||
                Rank == LOLRank.Platinum4 ||
                Rank == LOLRank.Platinum5) {
            color = PLATINUM;
            color = ContextCompat.getColor(context, R.color.PLATINUM);
        }
        else if (Rank == LOLRank.Challenger) {
            color = CHALLENGER;
            color = ContextCompat.getColor(context, R.color.CHALLENGER);
        }
        else if (Rank == LOLRank.Master) {
            color = MASTER;
            color = ContextCompat.getColor(context, R.color.MASTER);
        }
        else {
            color = BLACK;
        }
        return color;
    }

    public Drawable getRankGraphic(Context context) {
        if (this.drawable != null) {
            return this.drawable;
        }

        if (Rank == LOLRank.Bronze1 ||
                Rank == LOLRank.Bronze2 ||
                Rank == LOLRank.Bronze3 ||
                Rank == LOLRank.Bronze4 ||
                Rank == LOLRank.Bronze5) {
            drawable = context.getResources().getDrawable(R.drawable.bronzei);
        }
        else if(Rank == LOLRank.Silver1 ||
                Rank == LOLRank.Silver2 ||
                Rank == LOLRank.Silver3 ||
                Rank == LOLRank.Silver4 ||
                Rank == LOLRank.Silver5) {
            drawable = context.getResources().getDrawable(R.drawable.silver);
        }
        else if(Rank == LOLRank.Gold1 ||
                Rank == LOLRank.Gold2 ||
                Rank == LOLRank.Gold3 ||
                Rank == LOLRank.Gold4 ||
                Rank == LOLRank.Gold5) {
            drawable = context.getResources().getDrawable(R.drawable.gold);
        }
        else if(Rank == LOLRank.Diamond1 ||
                Rank == LOLRank.Diamond2 ||
                Rank == LOLRank.Diamond3 ||
                Rank == LOLRank.Diamond4 ||
                Rank == LOLRank.Diamond5) {
            drawable = context.getResources().getDrawable(R.drawable.diamondi);
        }
        else if(Rank == LOLRank.Platinum1 ||
                Rank == LOLRank.Platinum2 ||
                Rank == LOLRank.Platinum3 ||
                Rank == LOLRank.Platinum4 ||
                Rank == LOLRank.Platinum5) {
            drawable = context.getResources().getDrawable(R.drawable.platinumv);
        }
        else if (Rank == LOLRank.Challenger) {
            drawable = context.getResources().getDrawable(R.drawable.challenger);
        }
        else if (Rank == LOLRank.Master) {
            drawable = context.getResources().getDrawable(R.drawable.master);
        }
        else {
            drawable = context.getResources().getDrawable(R.drawable.white);
        }
        return drawable;
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
