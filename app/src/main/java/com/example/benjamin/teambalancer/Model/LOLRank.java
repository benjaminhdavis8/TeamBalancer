package com.example.benjamin.teambalancer.Model;

import android.widget.LinearLayout;

public enum LOLRank {

    UNRANKED,
    Bronze5,
    Bronze4,
    Bronze3,
    Bronze2,
    Bronze1,
    Silver5,
    Silver4,
    Silver3,
    Silver2,
    Silver1,
    Gold5,
    Gold4,
    Gold3,
    Gold2,
    Gold1,
    Platinum5,
    Platinum4,
    Platinum3,
    Platinum2,
    Platinum1,
    Diamond5,
    Diamond4,
    Diamond3,
    Diamond2,
    Diamond1,
    Master,
    fake1,
    fake2,
    fake3,
    fake4,
    fake5,
    Challenger;

    @Override
    public String toString() {
        String ret = name();
        if (ret.matches(".*[0-9]{1}")) {
            return ret; // TODO space seperate of a digit at the last position
        }
        return ret;
    }

    public boolean betterThan(LOLRank other) {
        return this.ordinal() > other.ordinal();
    }
}
