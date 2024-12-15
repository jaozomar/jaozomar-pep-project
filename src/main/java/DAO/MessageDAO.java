package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /** 
     * insert a message into the Message table
     * @param Message msg
     * @return Message msg with the generated message id if successful. Otherwise return null
    */
    public Message insertMessage(Message msg) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by,message_text,time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            // Set the column values we will add to the Message table
            ps.setInt(1,msg.getPosted_by());
            ps.setString(2,msg.getMessage_text());
            ps.setLong(3,msg.getTime_posted_epoch());
            ps.executeUpdate();

            // The generated keys restult set. Should contain the new message_id that was automatically generated
            ResultSet res = ps.getGeneratedKeys();
            if(res.next()) {
                msg.setMessage_id(res.getInt("message_id"));
                return msg; //return msg with the generated message_id
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get a list of all messages in the table
     * @param none
     * @return ArrayList<Message> of Message objects
    */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message;";
            Statement stmt = conn.createStatement();
            
            //ResultSet with all records from query
            ResultSet res = stmt.executeQuery(sql);

            //add a Message object with corresponding info for every record of ResultSet into the ArrayList
            while(res.next()) {
                messages.add(new Message(
                    res.getInt("message_id"),
                    res.getInt("posted_by"),
                    res.getString("message_text"),
                    res.getLong("time_posted_epoch")
                ));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * get a list of all messages in the table corresponding to a specific user
     * @param int postedBY 
     * @param account id, represents the foreign key posted_by
     * @return ArrayList<Message> of Message objects
    */
    public List<Message> getMessagesByUser(int postedBy) {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by=?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            //Set posted_by account id with our given postedBy value
            ps.setInt(1,postedBy);

            //ResultSet with all message records corresponding to the postedBY account it
            ResultSet res = ps.executeQuery();

            //add a Message object with corresponding info for every record of ResultSet into the ArrayList
            while(res.next()) {
                messages.add(new Message(
                    res.getInt("message_id"),
                    res.getInt("posted_by"),
                    res.getString("message_text"),
                    res.getLong("time_posted_epoch")
                ));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * search for a message by it's message_id
     * @param int msgID representing the message_id
     * @return a Message object of the matching record if found. Otherwise null
    */
    public Message getMessageByID(int msgID) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id=?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            //Set input message id in the prepared statement
            ps.setInt(1,msgID);

            //ResultSet with the record from the table that has a matching id, if it exists
            ResultSet res = ps.executeQuery();
            //If a matching record was found, return it
            while(res.next()) {
                return new Message(
                    res.getInt("message_id"),
                    res.getInt("posted_by"),
                    res.getString("message_text"),
                    res.getLong("time_posted_epoch")
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null; //Return an empty message if none found
    }

    /**
     * delete a message from the Message table
     * @param int msgID
     * @return void
    */
    public void deleteMessage(int msgID) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM Message WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            //set id of message to delete
            ps.setInt(1,msgID);
            //delete record
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update a message's text from the Message table
     * @param String msgText for the update text
     * @param int msgID for the id of the message to update
     * @return void
    */
    public void updateText(String msgText, int msgID) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text=? WHERE message_id=?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the replacement message_text and the message_id to update
            ps.setString(1,msgText);
            ps.setInt(2,msgID);
            //update record
            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
