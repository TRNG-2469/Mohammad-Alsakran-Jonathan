import Controller.DepartmentController;
import Controller.ReimbursementController;
import Controller.UsersController;
import exceptions.ErrorResponse;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClass {
    private static final Logger logger = LoggerFactory.getLogger(MainClass.class);
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
        DepartmentController departmentController = new DepartmentController();
        ReimbursementController reimbursementController = new ReimbursementController();
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/static");
        }).start(7700);


        //1. Retrieve all Users
        app.get("/api/Users", userController::getAllUsers);

        //2. Retrieve User based on id
        app.get("/api/Users/{id}", userController::getUserByID);

        //3. POST user data - create
        app.post("/api/Users", userController::createUser);

        //4. UPDATE user data
        app.put("/api/Users/{id}", userController::updateUser);

        //5. Delete  user data
        app.delete("/api/Users/{id}", userController::deleteUser);

        //6. Logout (I guess i'ts a user endpoint?)
        app.post("/api/logout", userController::logOut);

        //~~~~~~~~~~~~~~~~~~~~~~Department Routes~~~~~~~~~~~~~~~~~~~~~

        //1. Retrieve all Departments
        app.get("/api/Departments", departmentController::getAllDepartments);

        //2. Retrieve Department based on id
        app.get("/api/Departments/{id}", departmentController::getDepartmentById);

        //3. POST Department data - create
        app.post("/api/Departments", departmentController::createDepartment);

        //~~~~~~~~~~~~~~~~~~~~~~Login Route~~~~~~~~~~~~~~~~~~~~~~~~~~~

        app.post("/api/login", userController::logIn);


        //~~~~~~~~~~~~~~~~~~~~~~Reimbursement Route~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //1. Retrieve all Reimbursements
        app.get("/api/Reimbursements", reimbursementController::getAllReimbursements);

        //2. Retrieve Reimbursement based on status and department id
        // filter must be registered before {id} to avoid route collision!!!!!!!!!!!!!!!---------------------------------WARNING!
        app.get("/api/Reimbursements/filter", reimbursementController::getReimbursementsByStatusAndDepartment);

        //3. Retrieve Reimbursement based on id
        app.get("/api/Reimbursements/{id}", reimbursementController::getReimbursementByID);

        //4. POST Reimbursement data - create
        app.post("/api/Reimbursements", reimbursementController::createReimbursement);

        //5. UPDATE Reimbursement data
        app.put("/api/Reimbursements/{id}", reimbursementController::updateReimbursement);

        //6. Resolve Reimbursement data
        app.put("/api/Reimbursements/{id}/resolve", reimbursementController::resolveReimbursement);

        //7. Retrieve Reimbursement based on author id
        app.get("/api/Reimbursements/author/{id}", reimbursementController::getReimbursementByAuthor);


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Session Role-check Middlewear~~~~~~~~~~~~~~~~~~~~~~~~~
        app.before("/api/Reimbursements/{id}/resolve", ctx -> {
            Boolean role = ctx.sessionAttribute("role");
            if (role == null || !role) {
                ctx.status(403);
                ctx.json(new ErrorResponse("Only managers can resolve reimbursements."));
                ctx.skipRemainingHandlers();
            }
        });





        app.get("/api/me", userController::getCurrentUser);





        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ promoting employees to managers (only way to create managers)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        app.put("/api/Users/{id}/role", userController::updateUserRole);

        app.before("/api/Users/{id}/role", ctx -> {
            Boolean role = ctx.sessionAttribute("role");
            if (role == null || !role) {
                ctx.status(403);
                ctx.json(new ErrorResponse("Only managers can change user roles."));
                ctx.skipRemainingHandlers();
            }
        });


        app.exception(IllegalArgumentException.class,( e, ctx) -> {

            logger.warn("Validation error", e);
            ctx.status(400);
            ctx.json(new ErrorResponse("An unexpected Error occured."));
        });

        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Unexpected error", e);
            ctx.status(500);
            ctx.json(new ErrorResponse("An unexpected Server Error occured."));
        });
    }
}
