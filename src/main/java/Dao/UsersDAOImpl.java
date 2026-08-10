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
    public Users create(Users user) {
        String createSQL = "INSERT INTO Users (username, password, role, first_name, last_name, department_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING user_id";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(createSQL)) {
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getPassword());
            prepStatement.setBoolean(3, user.isRole());
            prepStatement.setString(4, user.getFirst_name());
            prepStatement.setString(5, user.getLast_name());
            prepStatement.setInt(6, user.getDepartment_id());

            var rs = prepStatement.executeQuery();
            if (rs.next()) {
                user.setUser_id(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void update(Users user) {
        String updateSQL = "UPDATE Users SET username=?, role=?, first_name=?, last_name = ?, department_id = ? WHERE user_id=?";

        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(updateSQL))
        {
            prepStatement.setString(1, user.getUsername());
            prepStatement.setBoolean(2, user.isRole());
            prepStatement.setString(3, user.getFirst_name());
            prepStatement.setString(4, user.getLast_name());
            prepStatement.setInt(5, user.getDepartment_id());
            prepStatement.setInt(6, user.getUser_id());
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
    public Users findByUsername(String username) {
        String findByUsernameSQL = "SELECT * FROM Users WHERE username=?";
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findByUsernameSQL)) {
            prepStatement.setString(1, username);
            var resultSet = prepStatement.executeQuery();

            if(resultSet.next()) {
                Users user = new Users();
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setRole(resultSet.getBoolean("role"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setDepartment_id(resultSet.getInt("department_id"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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
    public List<Users> findAll() { //no password, for security
        String findAllSQL = "SELECT * FROM Users";
        List<Users> mylist = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findAllSQL)) {
            var resultSet = prepStatement.executeQuery();
            while(resultSet.next()){
                Users user = new Users();
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
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
