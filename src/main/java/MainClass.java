import Controller.UsersController;
import Dao.UsersDAO;
import Dao.UsersDAOImpl;
import Model.Users;
import exceptions.ErrorResponse;
import io.javalin.Javalin;

public class MainClass {
    public static void main(String[] args) {
        /*
            //Main -> Controller -> Service -> DAO -> DB
        //Create
        UsersDAO userDAO = new UsersDAOImpl();

        Users user = new Users(2, "NewEmployee", "password2", false,"New", "Employee", 1 );
        userDAO.create(user);



        //Update
        UsersDAO userDAO = new UsersDAOImpl();

        Users user = new Users(2, "SecondUser", "password3", false, "Second","User", 1);
        userDAO.update(user);

        //Delete
        UsersDAO userDAO = new UsersDAOImpl();
        userDAO.delete(2);

        //FindAll
        UsersDAO userDAO = new UsersDAOImpl();
        System.out.println(userDAO.findAll());
         */

        UsersController userController = new UsersController();
        Javalin app = Javalin.create().start(7700);


        //1. Retrieve all Users
        app.get("/api/Users", userController::getAllUsers);

        //2. Retrieve User based on id
        app.get("/api/Users/{id}", userController::getUserByID);

        //3. POST user data - create
        app.post("/api/Users", userController::createUser);

        //4. UPDATE user data
        app.put("api/Users/{id}", userController::updateUser);

        //5. Delete  user data
        app.delete("/api/Users/{id}", userController::deleteUser);

        app.exception(IllegalArgumentException.class,( e, ctx) -> {
            ctx.status(400);
            ctx.json(new ErrorResponse("An unexpected Error occured."));
        });

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.json(new ErrorResponse("An unexpected Server Error occured."));
        });
    }
}
