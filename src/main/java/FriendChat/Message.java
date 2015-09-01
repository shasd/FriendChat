package FriendChat;

import java.time.LocalDateTime;

/**
 * Created by sukret on 8/11/15.
 */
public class Message {
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime time;

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Message(String sender, String receiver, String content, LocalDateTime time) {

        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }
}
