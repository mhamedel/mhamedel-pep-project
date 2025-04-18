package Controller;

import java.util.Map;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

    // Initializes and starts the Javalin API with defined endpoints
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handleCreateMessage); 
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleMessagesByUser);
        return app;
    }


    // Handles HTTP POST request for user registration
    private void handleRegister(Context ctx) {
        // Parse the JSON request body into an Account object
        Account acc = ctx.bodyAsClass(Account.class);

        // Attempt to register the account using the service
        Account result = accountService.register(acc);

        if (result != null) {
            // If registration is successful
            // Return the created account as JSON
            ctx.json(result);
        } else {
            // If registration fails
            // Return HTTP 400 Bad Request
            ctx.status(400);
        }
    }


    // Handles user login by validating provided credentials
    private void handleLogin(Context ctx) {
        // Extract account data (username, password) from the request body
        Account acc = ctx.bodyAsClass(Account.class);

        // Attempt to log in using the AccountService by checking credentials
        Account result = accountService.login(acc);

        // If the login is successful (result is not null), return the account details as JSON
        if (result != null) {
            ctx.json(result);
        } else {
            // If login fails, respond with a 401 Unauthorized status
            ctx.status(401);
        }
    }


    // Handles message creation by validating input and calling the message service to create a message
    private void handleCreateMessage(Context ctx) {
        Message msg = ctx.bodyAsClass(Message.class);

        // Attempt to create the message using the MessageService
        Message result = messageService.createMessage(msg);
        // If message creation is successful
        // Return the created message as JSON
        if (result != null) {
            ctx.json(result);
        } else {
            // If message creation fails (invalid input)
            // respond with a 400 Bad Request status
            ctx.status(400);
        }
    }

    // Retrieves and returns all messages from the message service
    private void handleGetAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }


    // Retrieves and returns a specific message by its ID
    private void handleGetMessageById(Context ctx) {
        // Extract the message ID from the path parameter
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message msg = messageService.getMessageById(message_id);

        // If the message is found
        if (msg != null) {
            ctx.json(msg);
        } else {
            ctx.json(""); // Empty response if the message doesn't exist
        }
    }


    // Handles message deletion by ID and returns the deleted message
    private void handleDeleteMessage(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message deleted = messageService.deleteMessageById(message_id);

        // If the message is deleted successfully
        // Return the deleted message as JSON
        if (deleted != null) {
            ctx.json(deleted);
        } else {
            ctx.json(""); // Empty response if deletion fails
        }
    }


    // Handles message update (updating message text) by ID and returns the updated message
    private void handleUpdateMessage(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        // Extract the new message text from the request body
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String newText = body.get("message_text");

        // Attempt to update the message text using the MessageService
        Message updated = messageService.updateMessageText(message_id, newText);

        // If the update is successful
        // return the updated message as JSON
        if (updated != null) {
            ctx.json(updated);
        } else {
            // If the update fails (invalid text)
            // Respond with a 400 Bad Request status
            ctx.status(400);
        }
    }

    // Retrieves and returns messages posted by a specific user by account ID
    private void handleMessagesByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByAccountId(accountId));
    }
 
    
    
}