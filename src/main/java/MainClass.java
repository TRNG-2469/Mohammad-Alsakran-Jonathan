import Dao.UsersDAO;
import Dao.UsersDAOImpl;
import Model.Users;

public class MainClass {
    public static void main(String[] args) {
        /*

        //Create
        UsersDAO userDAO = new UsersDAOImpl();

        Users user = new Users(2, "NewEmployee", "password2", false,"New", "Employee", 1 );
        userDAO.create(user);



        //Update
        UsersDAO userDAO = new UsersDAOImpl();

        Users user = new Users(2, "SecondUser", "password3", false, "Second","User", 1);
        userDAO.update(user);



        //FindAll
        UsersDAO userDAO = new UsersDAOImpl();
        System.out.println(userDAO.findAll());
         */
        UsersDAO userDAO = new UsersDAOImpl();
        userDAO.delete(2);
    }
}
