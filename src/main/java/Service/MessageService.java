package Service;

import Model.*;
import DAO.*;

import java.util.List;

public class MessageService {
    private final AccountDAO accDAO;
    private  final MessageDAO msgDAO;

    public MessageService() {
        accDAO = new AccountDAO();
        msgDAO = new MessageDAO();
    }

    public Message addMessage(Message msg) {
        if(msg.getMessage_text() == null || msg.getMessage_text().length() < 1) //message_text can't be empty
            return null;
        else if(msg.getMessage_text().length() > 255) //message_txt can't be over 255 characters
            return null;
        else if(!accDAO.doesIDExist(msg.getPosted_by())) //an account with account_id referenced by posted_by must exist
            return null;
        else
            return msgDAO.insertMessage(msg);
    }

    public List<Message> getAllMessages() {
        return msgDAO.getAllMessages();
    }
}