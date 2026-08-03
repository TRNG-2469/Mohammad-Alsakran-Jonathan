package Dao;

import Model.Users;
import Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAOImpl implements UsersDAO{
    @Override
    public void create(Users user) {
        String createSQL = "INSERT INTO Users (user_id, username, password, role, first_name, last_name, department_id) values(?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(createSQL))
        {
            prepStatement.setInt(1, user.getUser_id());
            prepStatement.setString(2, user.getUsername());
            prepStatement.setString(3, user.getPassword());
            prepStatement.setBoolean(4, user.isRole());
            prepStatement.setString(5, user.getFirst_name());
            prepStatement.setString(6, user.getLast_name());
            prepStatement.setInt(7, user.getDepartment_id());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Users user) {
        String updateSQL = "UPDATE Users SET username=?, password=?, role=?, first_name=?, last_name = ?, department_id = ? WHERE user_id=?";

        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(updateSQL))
        {
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getPassword());
            prepStatement.setBoolean(3, user.isRole());
            prepStatement.setString(4, user.getFirst_name());
            prepStatement.setString(5, user.getLast_name());
            prepStatement.setInt(6, user.getDepartment_id());
            prepStatement.setInt(7, user.getUser_id());
            prepStatement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String deleteSQL = "DELETE FROM Users WHERE user_id=?";

        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(deleteSQL))
        {
            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Users findById(int id) {
        String findOneSQL = "SELECT * FROM Users WHERE user_id=?";
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findOneSQL)) {
            prepStatement.setInt(1, id);
            var resultSet = prepStatement.executeQuery();

            if(resultSet.next()) {
                Users user = new Users();
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getBoolean("role"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setDepartment_id(resultSet.getInt("department_id"));
                return user;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Users> findAll() { //no password, apply to other methods if successful
        String findAllSQL = "SELECT * FROM Users";
        List<Users> mylist = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findAllSQL)) {
            var resultSet = prepStatement.executeQuery();
            while(resultSet.next()){
                Users user = new Users();
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getBoolean("role"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setDepartment_id(resultSet.getInt("department_id"));

                mylist.add(user);
            }
            return mylist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
