import Dao.UsersDAO;
import Dao.UsersDAOImpl;

public class MainClass {
    public static void main(String[] args) {
        //FindAll
        UsersDAO userDAO = new UsersDAOImpl();
        System.out.println(userDAO.findAll());
    }
}
