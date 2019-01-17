package mason.and.benjamin.teambalancer.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

public class FriendsList {
    List<Friend> friendsList;

    public final String PREF_NAME = "friends";
    public final String PREF_SET_NAME = "set";
    public final String DELIMITER = "/";

    private static Context context;

    private static FriendsList instance = new FriendsList();

    private FriendsList() {

        friendsList = new ArrayList<>();

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            Set<String> fetch = prefs.getStringSet(PREF_SET_NAME, null);
            for (String s : fetch) {
                String[] parts = s.split(DELIMITER);
                String username = parts[0];
                String rank = parts[1];
                int rankNum = Integer.parseInt(rank);
                LOLRank lolrank = LOLRank.values()[rankNum];

                friendsList.add(new Friend(username, lolrank));
            }
        }
        else {

            /*
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
            */
        }
    }

    public static FriendsList getInstance(Context contxt) {
        context = contxt;

        return instance;
    }

    public void addFriend(Friend f)
    {
        friendsList.add(f);
    }

    public List<Friend> getFriends()
    {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            Set<String> canNullFriendSet = prefs.getStringSet(PREF_SET_NAME, null);
            if (canNullFriendSet == null) {
                canNullFriendSet = new HashSet<>();
            }
            Set<String> FriendSet = new HashSet<>(canNullFriendSet);

            for (String s : FriendSet) {
                String[] parts = s.split(DELIMITER);
                String username = parts[0];
                String rank = parts[1];
                int rankNum = Integer.parseInt(rank);
                LOLRank lolrank = LOLRank.values()[rankNum];

                boolean containsSumm = false;
                for (Friend summ : friendsList) {
                    if (summ.getUsername().equals(username)) {
                        containsSumm = true;
                    }
                }
                if (!containsSumm) {
                    friendsList.add(new Friend(username, lolrank));
                }
            }
            sortFriends();
        }
        return friendsList;
    }

    public void removeFriend(Friend f)
    {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            Set<String> canNullFriendSet = prefs.getStringSet(PREF_SET_NAME, null);
            if (canNullFriendSet == null) {
                canNullFriendSet = new HashSet<>();
            }
            Set<String> FriendSet = new HashSet<>(canNullFriendSet);


            String removeString = "";
            for (String s : FriendSet) {
                String[] parts = s.split(DELIMITER);
                String username = parts[0];

                if (username.equals(f.getUsername())) {
                    removeString = s;
                }
            }
            if (!removeString.equals("")) {
                FriendSet.remove(removeString);
            }

            editor.putStringSet(PREF_SET_NAME, FriendSet);
            editor.apply();

            sortFriends();
        }

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
