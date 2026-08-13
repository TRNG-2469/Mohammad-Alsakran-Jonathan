package Dao;

import Model.Users;

import java.util.List;

public interface UsersDAO {
    Users create(Users user);
    void update(Users user);
    void delete(int id);
    Users findByUsername(String username);
    Users findById(int id);
    List<Users> findAll();
    void updateRole(int id, boolean role); //----------promoting employees to managers
}
