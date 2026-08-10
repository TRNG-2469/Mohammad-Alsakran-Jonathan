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
        ctx.json(userService.findById(id));
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
        Users payload = ctx.bodyAsClass(Users.class);
        Users user = userService.logIn(payload.getUsername(), payload.getPassword());
        if(user != null){
            ctx.status(200);
            ctx.json(user);
        } else {
            ctx.status(401);
            ctx.json(new ErrorResponse("Invalid username or password"));
        }
    }


}
