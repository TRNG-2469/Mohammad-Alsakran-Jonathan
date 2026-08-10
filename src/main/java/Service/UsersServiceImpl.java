package Service;

import Dao.UsersDAO;
import Dao.UsersDAOImpl;
import Model.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UsersServiceImpl implements UsersService{

    private final UsersDAO userDAO = new UsersDAOImpl();
    @Override
    public Users create(Users user) {
        if(user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getLast_name() == null || user.getLast_name().isEmpty()){
            throw new IllegalArgumentException("First and last name cannot be null.");
        }
        if(user.getUsername() == null || user.getUsername().length() < 3 || user.getUsername().length() > 30){
            throw new IllegalArgumentException("Username must be between 3 and 30 characters.");
        }
        if(user.getPassword() == null || user.getPassword().length() < 8){
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }
        if(userDAO.findByUsername(user.getUsername()) != null){
            throw new IllegalArgumentException("Username already taken.");
        }

        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        return userDAO.create(user);
    }

    @Override
    public Users update(Users user) {
        if(user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getLast_name() == null || user.getLast_name().isEmpty()){
            throw new IllegalArgumentException("First and last name cannot be null.");
        }

        userDAO.update(user);
        return user;
    }

    @Override
    public void delete(int id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be <= 0");
        }
        userDAO.delete(id);
    }

    @Override
    public Users findByUsername(String username) {
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        return userDAO.findByUsername(username);
    }



    @Override
    public Users findById(int id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be <= 0");
        }
        return userDAO.findById(id);
    }

    @Override
    public List<Users> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Users logIn(String username, String password) {
        Users user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            user.setPassword(null);
            return user;
        }
        return null;
    }


}
