package Service;

import Model.Users;

import java.util.List;

public interface UsersService {
    Users create(Users user);
    Users update(Users user);
    void delete(int id);

    Users findById(int id);
    List<Users> findAll();
}
