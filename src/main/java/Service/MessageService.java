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

    /**
     * Add a new message to the Message table
     * @param Message msg
     * @return Message object of the message that was added. If fails to add, return null
     */
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

    /**
     * Return a list of all messages in the Message table
     * @param none
     * @return List<Message> of Message objects
     */
    public List<Message> getAllMessages() {
        return msgDAO.getAllMessages();
    }

    /**
     * Return a Message object that corresponds to a given message_id
     * @param int msgID
     * @return Message object with matching id if found
     */
    public Message getMessageByID(int msgID) {
        return msgDAO.getMessageByID(msgID);
    }

    /**
     * DElete a message from the table and return the message that was deleted
     * @param int msgID
     * @return Message object with message that was deleted. null if no message deleted.
     */
    public Message deleteMessage(int msgID) {
        Message messageToDelete = msgDAO.getMessageByID(msgID);
        if(messageToDelete == null) //return null if message not found
            return null;
        msgDAO.deleteMessage(msgID); //delete message
        return messageToDelete;
    }

    /**
     * Update the message_text of a message with a given message_id
     * @param String msgText
     * @param int msgID
     * @return Message object for the updated message. If no update happens, returns null
     */
    public Message updateMessage(String msgText, int msgID) {
        Message messageToUpdate = msgDAO.getMessageByID(msgID);
        if(messageToUpdate == null) //return null if message not found
            return null;
        //return null if new text is empty or longet than 255 characters
        if(msgText == null || msgText.length() < 1 || msgText.length() > 255)
            return null;
        msgDAO.updateText(msgText, msgID); //update message
        messageToUpdate.setMessage_text(msgText); //update message text in the Message object we will return
        return messageToUpdate;
    }

    /**
     * Get a list of all messages that are posted by a specified account
     * @param int accID representing the account id
     * @return List<Message> of Message objects
     */
    public List<Message> getMessagesByUser(int accID) {
        return msgDAO.getMessagesByUser(accID);
    }
}