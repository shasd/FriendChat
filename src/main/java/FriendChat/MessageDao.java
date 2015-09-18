package FriendChat;

import java.util.List;

/**
 * Created by sukret on 8/11/15.
 * Interface for message storage service
 */
public interface MessageDao {

    boolean add(Message message);

    List<Message> get(String sender, String receiver);

    List<Message> get(String handle);

    List<Message> get(String handle, int period);

}
