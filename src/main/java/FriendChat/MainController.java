package FriendChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/message/")
public class MainController {

    private static final int MESSAGE_TIME_WINDOW = 5; // duration of time of messages sent to message requester in seconds
    @Autowired
    MessageDao messageDao;

    @Autowired
    FriendRecommender friendRecommender;

    private Logger logger = LoggerFactory.getLogger(MainController.class);


    @RequestMapping(value = "send/{sender}/{receiver}", method = RequestMethod.POST)
    public @ResponseBody String send(@PathVariable("sender") String sender,
                                     @PathVariable("receiver") String receiver,
                                     @RequestBody String message) throws UnsupportedEncodingException{
        String decodedMessage = URLDecoder.decode(message.substring(0, message.length() - 1), "UTF-8");
        messageDao.add(new Message(sender, receiver, decodedMessage, LocalDateTime.now()));
        friendRecommender.addMessage(sender, decodedMessage);
        return "Message sent";
    }

    @RequestMapping(value = "receiveRecent/{handle}", method = RequestMethod.GET)
    public @ResponseBody List<Message> getByReceiverRecent(@PathVariable("handle") String handle) {
        return messageDao.get(handle, MESSAGE_TIME_WINDOW);
    }

    @RequestMapping(value = "receive/{handle}", method = RequestMethod.GET)
    public @ResponseBody List<Message> getByReceiver(@PathVariable("handle") String handle) {
        return messageDao.get(handle);
    }

    @RequestMapping(value = "recommend/{handle}", method = RequestMethod.GET)
    public @ResponseBody String recommendFriend(@PathVariable("handle") String handle) {
        return friendRecommender.getBestFriend(handle);
    }
}
