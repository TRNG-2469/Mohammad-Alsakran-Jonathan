package Controller;

import Model.Users;
import Service.UsersServiceImpl;
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

        //make a check for null
       // if(payload.getName() == null || payload.getSalary() <= 0){
         //   throw new NullPointerException("Name cannot be null, salary cannot be <= 0");
        //}
        //password should not be updated?
        Users updated = new Users(id, payload.getUsername(), payload.getPassword(), payload.isRole(), payload.getFirst_name(), payload.getLast_name(), payload.getDepartment_id());
        ctx.json(userService.update(updated));
    }

    public void deleteUser(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        userService.delete(id);
        ctx.status(204);
    }
}
