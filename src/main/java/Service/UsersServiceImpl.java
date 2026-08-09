package Service;

import Dao.UsersDAO;
import Dao.UsersDAOImpl;
import Model.Users;

import java.util.List;

public class UsersServiceImpl implements UsersService{

    private final UsersDAO userDAO = new UsersDAOImpl();
    @Override
    public Users create(Users user) {

        if(user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getLast_name() == null || user.getLast_name().isEmpty()){
            throw new IllegalArgumentException("First and last name cannot be null.");
        }
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
}
