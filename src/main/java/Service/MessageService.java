package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    // Creating an instance of MessageDAO to interact with the database
    private MessageDAO messageDAO = new MessageDAO();

    // Method to create a new message with validation
    public Message createMessage(Message message) {
        // Check if the message text is null, blank, or over 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()
            || message.getMessage_text().length() > 255) {
            return null; 
        }
        // Call DAO to insert the message into the database
        return messageDAO.insertMessage(message);
    }


    // Method to retrieve all messages from the database
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages(); 
    }


    // Method to get a single message by its ID
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id); // Retrieve message using DAO
    }


    // Method to delete a message by ID
    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessage(id); 
    }


    // Method to update the text of a message
    public Message updateMessageText(int id, String newText) {
        // Validate the new message text
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null; 
        }
        return messageDAO.updateMessage(id, newText); 
    }

    
    // Method to get all messages posted by a specific user id
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}
