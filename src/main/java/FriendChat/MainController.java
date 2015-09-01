package FriendChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import FriendChat.exceptions.StorageLimitExceededException;
import FriendChat.exceptions.UserDoesNotExistException;
import FriendChat.exceptions.UserExistsException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/message/")
public class MainController {

    private static final int MESSAGE_TIME_WINDOW = 5; // duration of time of messages sent to message requester in seconds
    @Autowired
    MessageDao messageDao;

    private Logger logger = LoggerFactory.getLogger(MainController.class);


    @RequestMapping(value = "send/{sender}/{receiver}", method = RequestMethod.POST)
    public @ResponseBody String send(@PathVariable("sender") String sender,
                                     @PathVariable("receiver") String receiver,
                                     @RequestBody String message) {
        messageDao.add(new Message(sender, receiver, message.substring(0,message.length() - 1), LocalDateTime.now()));
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

}
