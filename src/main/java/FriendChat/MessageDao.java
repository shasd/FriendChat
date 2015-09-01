package FriendChat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import FriendChat.exceptions.UserDoesNotExistException;
import FriendChat.exceptions.UserExistsException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by sukret on 8/11/15.
 */
public interface MessageDao {

    boolean add(Message message);

    List<Message> get(String sender, String receiver);

    List<Message> get(String handle);

    List<Message> get(String handle, int period);

}
