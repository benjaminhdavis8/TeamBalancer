package com.example.benjamin.teambalancer.Model;

import java.util.ArrayList;
import java.util.List;

public class FriendsList {
    List<Friend> friendsList;

    private static FriendsList instance = new FriendsList();

    private FriendsList() {
        friendsList = new ArrayList<>();
        friendsList.add(new Friend("Aragon", LOLRank.Gold3));
        friendsList.add(new Friend("Belthazar", LOLRank.Bronze3));
        friendsList.add(new Friend("Cheeser",LOLRank.Platinum2));
        friendsList.add(new Friend("ddog", LOLRank.Diamond1));
        friendsList.add(new Friend("Destruction", LOLRank.Bronze2));
        friendsList.add(new Friend("Faker", LOLRank.Master));
        friendsList.add(new Friend("Olock", LOLRank.Challenger));
        friendsList.add(new Friend("Phoenix", LOLRank.Bronze5));
        friendsList.add(new Friend("Shwartz", LOLRank.Silver1));
        friendsList.add(new Friend("Trident"));
        friendsList.add(new Friend("US Economy", LOLRank.Diamond4));
    }

    public static FriendsList getInstance() {
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

    public void sortFriends() {
        List<Friend> sortedlist = new ArrayList<>();
        String firstName;
        Friend firstPlayer;
        String currentName;
        while (friendsList.size() > 0) {
            firstName = "zzzzzzzzzzzzzzzzzzzzzzz";
            firstPlayer = friendsList.get(0);
            for (Friend f : friendsList) {
                currentName = f.getUsername();
                if (currentName.compareToIgnoreCase(firstName) < 0){
                    firstName = currentName;
                    firstPlayer = f;
                }
            }
            friendsList.remove(firstPlayer);
            sortedlist.add(firstPlayer);
        }
        friendsList = sortedlist;
    }

}
