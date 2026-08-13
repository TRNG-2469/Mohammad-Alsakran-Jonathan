package Service;

import Model.Users;

import java.util.List;

public interface UsersService {
    Users create(Users user);
    Users update(Users user);
    void delete(int id);
    Users findByUsername(String username);
    Users findById(int id);
    List<Users> findAll();

    //My idea for login (for now)
    Users logIn(String username, String password);

    void updateRole(int id, boolean role);
}
