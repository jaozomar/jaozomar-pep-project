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
    private final ObjectMapper om = new ObjectMapper();

    public SocialMediaController() {
        accService = new AccountService();
        msgService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registrationHandler);
        app.post("login",this::loginHandler);
        app.post("messages",this::messageCreationHandler);
        app.get("messages",ctx -> {
            ctx.status(200);
            ctx.json(msgService.getAllMessages());
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


}