package Dao;

import Model.Users;

import java.util.List;

public interface UsersDAO {
    void create(Users user);
    void update(Users user);
    void delete(int id);

    Users findById(int id);
    List<Users> findAll();
}
