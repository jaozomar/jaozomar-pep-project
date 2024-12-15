package Controller;

import Model.Account;
import Service.AccountService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accService;

    public SocialMediaController() {
        accService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::postHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postHandler(Context ctx) {
        try {
            ObjectMapper om = new ObjectMapper();
            Account inputAcc = om.readValue(ctx.body(), Account.class);
            if(inputAcc.getUsername() == null || inputAcc.getUsername().length() < 1) //username can't be empty
                ctx.status(400);
            else if(inputAcc.getPassword().length() < 4) //password must be atleast 4 chars long
                ctx.status(400);
            else {
                Account resultAcc = accService.addAccount(inputAcc); //this will insert into table or return null
                if(resultAcc == null) //fails if null
                    ctx.status(400);
                else {
                    ctx.status(200); //success status code
                    ctx.json(resultAcc);
                }
            }
        } catch(Exception e) {
            ctx.status(400);
        }
    }


}