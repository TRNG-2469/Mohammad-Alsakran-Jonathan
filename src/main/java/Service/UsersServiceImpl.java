package Service;

import Dao.UsersDAO;
import Dao.UsersDAOImpl;
import Model.Users;

import java.util.List;

public class UsersServiceImpl implements UsersService{
    @Override
    public Users create(Users user) {
        UsersDAO userDAO = new UsersDAOImpl();
        if(user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getLast_name() == null || user.getLast_name().isEmpty()){
            throw new IllegalArgumentException("First and last name cannot be null.");
        }
        return userDAO.create(user);
    }

    @Override
    public Users update(Users user) {
        UsersDAO userDAO = new UsersDAOImpl();
        if(user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getLast_name() == null || user.getLast_name().isEmpty()){
            throw new IllegalArgumentException("First and last name cannot be null.");
        }
        userDAO.update(user);
        return user;
    }

    @Override
    public void delete(int id) {
        UsersDAO userDAO = new UsersDAOImpl();
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be <= 0");
        }
        userDAO.delete(id);
    }

    @Override
    public Users findById(int id) {
        UsersDAO userDAO = new UsersDAOImpl();
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be <= 0");
        }
        return userDAO.findById(id);
    }

    @Override
    public List<Users> findAll() {
        UsersDAO userDAO = new UsersDAOImpl();
        return userDAO.findAll();
    }
}
