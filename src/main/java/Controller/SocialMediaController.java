package Controller;

import Model.*;
import Service.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accService;
    private final MessageService msgService;
    private final ObjectMapper om;

    public SocialMediaController() {
        accService = new AccountService();
        msgService = new MessageService();
        om = new ObjectMapper();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        //register a new account
        app.post("register", this::registrationHandler);

        //login to an account
        app.post("login",this::loginHandler);

        //create new message
        app.post("messages",this::messageCreationHandler);

        //get all messages
        app.get("messages",ctx -> {
            ctx.status(200);
            ctx.json(msgService.getAllMessages());
        });

        //get message by it's message id
        app.get("messages/{message_id}", this::searchMessageHandler);

        //delete message by it's message id
        app.delete("messages/{message_id}", this::deleteMessageHandler);

        //update the text for a message of a specified message id
        app.patch("messages/{message_id}", this::updateMessageHandler);

        //get all messages posted by a user of a specified account id
        app.get("accounts/{account_id}/messages", ctx -> {
            int accID = Integer.parseInt(ctx.pathParam("account_id")); //get the path parameter as an int
            ctx.status(200);
            ctx.json(msgService.getMessagesByUser(accID));
        });

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * handler for the account registration
     * @param context Context object to manage information about the request and response
     */
    private void registrationHandler(Context ctx) {
        try { //in case of issue when mapping
            Account inputAcc = om.readValue(ctx.body(), Account.class);
            Account resultAcc = accService.addAccount(inputAcc); //this will insert into table or return null
            if(resultAcc == null) //fails to add if null
                ctx.status(400);
            else {
                ctx.status(200);
                ctx.json(resultAcc);
            }
        } catch(Exception e) {
            ctx.status(400);
            e.printStackTrace();
        }
    }

    /**
     * handler for the account login
     * @param context Context object to manage information about the request and response
     */
    private void loginHandler(Context ctx) {
        try { //in case of issue when mapping
            Account inputAcc = om.readValue(ctx.body(), Account.class);
            Account resultAcc = accService.login(inputAcc); //will try to login with given info
            if(resultAcc == null) //fails to login if null
                ctx.status(401);
            else {
                ctx.status(200);
                ctx.json(resultAcc);
            }
        } catch(Exception e) {
            ctx.status(401);
            e.printStackTrace();
        }
    }

    /**
     * handler for the message creation
     * @param context Context object to manage information about the request and response
     */
    private void messageCreationHandler(Context ctx) {
        try { //in case of issue when mapping
            Message inputMsg = om.readValue(ctx.body(), Message.class);
            Message resultMsg = msgService.addMessage(inputMsg); //this will insert into table or return null
            if(resultMsg == null) //fails to add message if null
                ctx.status(400);
            else {
                ctx.status(200);
                ctx.json(resultMsg);
            }
        } catch(Exception e) {
            ctx.status(400);
            e.printStackTrace();
        }
    }

    /**
     * handler for searching messages with a given message id
     * @param context Context object to manage information about the request and response
     */
    private void searchMessageHandler(Context ctx) {
        int msgID = Integer.parseInt(ctx.pathParam("message_id")); //get the path parameter as an int
        Message resultMsg = msgService.getMessageByID(msgID);
        ctx.status(200);
        if(resultMsg != null)
            ctx.json(resultMsg); //return json if message was found
    }

    /**
     * handler for message deletion
     * @param context Context object to manage information about the request and response
     */
    private void deleteMessageHandler(Context ctx) {
        int msgID = Integer.parseInt(ctx.pathParam("message_id")); //get the path parameter as an int
        Message resultMsg = msgService.deleteMessage(msgID);
        ctx.status(200);
        if(resultMsg != null)
            ctx.json(resultMsg); //return json if message was deleted
    }

    /**
     * handler for the message text updates
     * @param context Context object to manage information about the request and response
     */
    private void updateMessageHandler(Context ctx) {
        try {
            String msgText = (om.readValue(ctx.body(), Message.class)).getMessage_text(); //get given message_text
            int msgID = Integer.parseInt(ctx.pathParam("message_id")); //get the path parameter as an int
            Message resultMsg = msgService.updateMessage(msgText, msgID);
            if(resultMsg == null)
                ctx.status(400);
            else {
                ctx.status(200);
                ctx.json(resultMsg);
            }
        } catch(Exception e) {
            ctx.status(400);
            e.printStackTrace();
        }
    }
}