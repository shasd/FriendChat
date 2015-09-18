package FriendChat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sukret on 8/31/15.
 * Stores messages in memory
 */
public class MemoryMessageDaoImpl implements MessageDao {

    List<Message> messages;

    public MemoryMessageDaoImpl() {
        messages = new ArrayList<Message>();
        System.out.println("initialized");
    }


    @Override
    public synchronized boolean add(Message message) {
        messages.add(message);
        System.out.println("adding message " + message.getContent());
        return true;
    }

    @Override
    public synchronized List<Message> get(String sender, String receiver) {
        return messages.stream().filter(message ->
                        message.getSender().equals(sender) && message.getReceiver().equals(receiver))
                       .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Message> get(String handle) {
        return messages.stream().filter(m -> m.getReceiver().equals(handle) || m.getSender().equals(handle))
                       .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Message> get(String handle, int period) {
        return messages.stream().filter(m -> (m.getReceiver().equals(handle) || m.getSender().equals(handle)) &&
                                              m.getTime().plusSeconds(period).isAfter(LocalDateTime.now()))
                       .collect(Collectors.toList());
    }
}
