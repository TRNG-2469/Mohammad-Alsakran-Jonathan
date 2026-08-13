package Controller;

import Model.Users;
import Service.UsersServiceImpl;
import exceptions.ErrorResponse;
import io.javalin.http.Context;

public class UsersController {
    private final UsersServiceImpl userService = new UsersServiceImpl();
    public void getAllUsers(Context ctx){
        //handler
        ctx.json(userService.findAll());
    }
    public void getUserByID(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Users user = userService.findById(id);
        if (user == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse("User not found."));
            return;
        }
        ctx.json(user);
    }



    public void createUser(Context ctx){
        Users payload = ctx.bodyAsClass(Users.class);
        Users newUser = userService.create(payload);
        ctx.status(201);
        ctx.json(newUser);
    }

    public void updateUser(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Users payload = ctx.bodyAsClass(Users.class);

        // password is intentionally excluded from general updates.
        // UsersDAOImpl.update() no longer touches the password column at all,
        // so this field is ignored regardless of what's passed here.
        // Password changes require a dedicated reset flow, not built yet.
        Users updated = new Users(id, payload.getUsername(), null, payload.isRole(), payload.getFirst_name(), payload.getLast_name(), payload.getDepartment_id());
        ctx.json(userService.update(updated));
    }

    public void deleteUser(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        userService.delete(id);
        ctx.status(204);
    }

    public void logIn(Context ctx){
        var credentials = ctx.basicAuthCredentials();
        Users user = userService.logIn(credentials.getUsername(), credentials.getPassword());
        if(user != null){
            ctx.status(200);
            ctx.sessionAttribute("user_id", user.getUser_id());
            ctx.sessionAttribute("role", user.isRole());
            ctx.json(user);
        } else {
            ctx.status(401);
            ctx.json(new ErrorResponse("Invalid username or password"));
        }
    }

    public void logOut(Context ctx){
        ctx.sessionAttribute("user_id", null);
        ctx.sessionAttribute("role", null);
    }



    public void getCurrentUser(Context ctx){ //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Checks real session and returns the current user (moving away from LocalStorage as teh source of truth)
        Integer userId = ctx.sessionAttribute("user_id");
        if (userId == null) {
            ctx.status(401);
            ctx.json(new ErrorResponse("Not logged in."));
            return;
        }
        Users user = userService.findById(userId);
        ctx.json(user);
    }






}
