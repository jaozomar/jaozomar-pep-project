package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // insert a message into the Message table
    // parameters: Message msg
    // return: Message msg with the generated message id if successful. Otherwise return null
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

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message;";
            Statement stmt = conn.createStatement();
            
            //ResultSet with all record from query
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
    
}
