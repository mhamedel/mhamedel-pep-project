package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Inserts a new message into the database and returns the message with generated ID
    public Message insertMessage(Message message) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ps.executeUpdate(); 
            /*  The method PreparedStatement.getGeneratedKeys() is used to retrieve the values of
                auto-generated keys—typically primary keys like id—after executing an INSERT statement.*/
            ResultSet rs = ps.getGeneratedKeys(); 
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1)); 
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL error if something goes wrong
        }
        return null; // Return null if insertion failed
    }



    // Retrieves all messages from the database
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            // Loop through results and build Message objects
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages; // Return the list of messages
    }



    // Retrieves a message by its ID
    public Message getMessageById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id); // Set the ID to search for
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // If found, return a new Message object
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if message is not found
    }


    
    // Deletes a message by ID and returns the deleted message
    public Message deleteMessage(int id) {
        Message msg = getMessageById(id); 
        if (msg != null) {
            try (Connection conn = ConnectionUtil.getConnection()) {
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id); 
                ps.executeUpdate(); 
                return msg; // Return the previously retrieved message
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if message does not exist or deletion fails
    }



    // Updates the message text by message ID and returns the updated message
    public Message updateMessage(int id, String newText) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newText); 
            ps.setInt(2, id); 
            
            int affected = ps.executeUpdate(); 
            if (affected > 0) {
                return getMessageById(id); // Return updated message if successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if update failed
    }



    // Retrieves all messages posted by a specific user (by account ID)
    public List<Message> getMessagesByUserId(int accountId) {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId); 
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages; // Return list of messages by the user
    }
}
