package FriendChat;

import java.util.*;

/**
 * Created by sukret on 9/1/15.
 * Recommends friends using the cosine similarity metric
 */
public class FriendRecommender {
    private Map<String, NavigableMap<String, Integer>> userWords; // maps of word counts for each user
    private Map<String, Double> userNorms; // norm of word count vector for each user
    public FriendRecommender() {
        userWords = new HashMap<>();
        userNorms = new HashMap<>();
    }

    public synchronized void addMessage(String user, String message) {
        String[] messageWords = message.split("\\s");

        NavigableMap<String, Integer> words = userWords.get(user);
        if(words == null) {  // create new user record
            words = new TreeMap<>();
            userWords.put(user, words);
        }
        for(String word: messageWords){
            String lowerCaseWord = word.toLowerCase();
            Integer oldCount = words.get(lowerCaseWord);
            if(oldCount == null) {
                words.put(lowerCaseWord, 1);
                if(userNorms.get(user) == null) {
                    userNorms.put(user, 1.0);
                } else {
                    userNorms.put(user, userNorms.get(user) + 1); // increment norm
                }
            }
            else {
                words.put(lowerCaseWord, oldCount + 1); // increment count of word
                userNorms.put(user, userNorms.get(user) + 2 * oldCount + 1 ); // increment norm
            }
        }
    }

    public synchronized String getBestFriend(String user) {
        NavigableMap<String, Integer> words = userWords.get(user);
        if(words == null)
            return user; // return yourself if nothing has been written
        String closestUser = user;
        double biggestSimilarity = -1;
        // iterate over all other users to find biggest similarity
        for(Map.Entry<String, NavigableMap<String, Integer>> otherUser: userWords.entrySet()) {
            if(otherUser.getKey().equals(user))
                continue;
            double product = 0;
            Iterator<Map.Entry<String, Integer>> otherUserIter = otherUser.getValue().entrySet().iterator();
            if(otherUserIter.hasNext()) { // iterate over the word count vector of the other user and this user together
                Map.Entry<String, Integer> otherUserEntry = otherUserIter.next();
                for (Map.Entry<String, Integer> thisUserEntry : words.entrySet()) {
                    while(thisUserEntry.getKey().compareTo(otherUserEntry.getKey()) > 0) {
                        if(otherUserIter.hasNext())
                            otherUserEntry = otherUserIter.next();
                        else
                            break;
                    }
                    if(thisUserEntry.getKey().equals(otherUserEntry.getKey())){
                        product += thisUserEntry.getValue() * otherUserEntry.getValue();
                    }

                }
            }
            // normalize dot product to get similarity
            double similarity = product / Math.sqrt(userNorms.get(user) * userNorms.get(otherUser.getKey()));
            if(similarity > biggestSimilarity) {
                biggestSimilarity = similarity;
                closestUser = otherUser.getKey();
            }
            System.out.println("Sim for user " + user + " and " + otherUser.getKey() + " is " + similarity);
        }
        return closestUser;
    }

}
