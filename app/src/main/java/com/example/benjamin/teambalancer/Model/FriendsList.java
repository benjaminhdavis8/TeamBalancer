package com.example.benjamin.teambalancer.Model;

import java.util.ArrayList;
import java.util.List;

public class FriendsList {
    List<Friend> friendsList;

    private static FriendsList instance = new FriendsList();

    private FriendsList() {
        friendsList = new ArrayList<>();
        friendsList.add(new Friend("Faker", LOLRank.Master));
        friendsList.add(new Friend("Shwartz", LOLRank.Silver1));
        friendsList.add(new Friend("US Economy", LOLRank.Diamond4));
        friendsList.add(new Friend("Phoenix", LOLRank.Bronze5));
        friendsList.add(new Friend("Olock", LOLRank.Challenger));
        friendsList.add(new Friend("ddog", LOLRank.Diamond1));
        friendsList.add(new Friend("Cheeser",LOLRank.Platinum2));
        friendsList.add(new Friend("Aragon", LOLRank.Bronze1));
        friendsList.add(new Friend("Destruction", LOLRank.Bronze2));
        friendsList.add(new Friend("Trident"));
        friendsList.add(new Friend("Belthazar", LOLRank.Bronze3));
    }

    public FriendsList getInstance() {
        return instance;
    }

    public void addFriend(Friend f)
    {
        friendsList.add(f);
    }

    public List<Friend> getFriends()
    {
        return friendsList;
    }

    public void removeFriend(Friend f)
    {
        friendsList.remove(f);
    }

}
