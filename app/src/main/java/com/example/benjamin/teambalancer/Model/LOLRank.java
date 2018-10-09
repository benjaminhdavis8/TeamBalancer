package com.example.benjamin.teambalancer.Model;

public enum LOLRank {
    Bronze1, Bronze2, Bronze3, Bronze4, Bronze5,
    Silver1, Silver2, Silver3, Silver4, Silver5,
    Gold1, Gold2, Gold3, Gold4, Gold5,
    Platinum1, Platinum2, Platinum3, Platinum4, Platinum5,
    Diamond1, Diamond2, Diamond3, Diamond4, Diamond5,
    Master,
    Challenger,
    Pending;

    @Override
    public String toString() {
        String ret = name();
        if (ret.matches(".*[0-9]{1}")) {
            return ret; // TODO space seperate of a digit at the last position
        }
        return ret;
    }
}
