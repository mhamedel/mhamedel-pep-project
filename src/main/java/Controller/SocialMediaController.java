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
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::handleRegister);
        // app.post("/login", this::handleLogin);
        // app.post("/messages", this::handleCreateMessage); 

        // app.get("/messages", this::handleGetAllMessages);

        // app.get("/messages/{id}", this::handleGetMessageById);

        // app.delete("/messages/{id}", this::handleDeleteMessage);

        // app.patch("/messages/{id}", this::handleUpdateMessage);

        // app.get("/accounts/{account_id}/messages", this::handleMessagesByUser);

        return app;
    }



  // Handles HTTP POST request for user registration
    private void handleRegister(Context ctx) {
        // Parse the JSON request body into an Account object
        Account acc = ctx.bodyAsClass(Account.class);

        // Attempt to register the account using the service
        Account result = accountService.register(acc);

        if (result != null) {
            // If registration is successful, return the created account as JSON
            ctx.json(result);
        } else {
            // If registration fails.  return HTTP 400 Bad Request
            ctx.status(400);
        }
    }



}