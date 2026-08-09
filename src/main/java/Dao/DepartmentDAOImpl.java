package Dao;

import Model.Department;
import Model.Users;
import Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {

    @Override
    public Department create(Department department) {
        String createSQL = "INSERT INTO Department (name) VALUES (?) RETURNING department_id";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(createSQL)) {
            prepStatement.setString(1, department.getName());
            var rs = prepStatement.executeQuery();
            if (rs.next()) {
                department.setDepartment_id(rs.getInt("department_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return department;
    }

    @Override
    public Department findById(int id) {
        String findOneSQL = "SELECT * FROM department WHERE department_id=?";
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findOneSQL)) {
            prepStatement.setInt(1, id);
            var resultSet = prepStatement.executeQuery();

            if(resultSet.next()) {
                Department department = new Department();
                department.setDepartment_id(resultSet.getInt("department_id"));
                department.setName(resultSet.getString("name"));
                return department;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;}

    @Override
    public List<Department> findAll() {
        String findAllSQL = "SELECT * FROM department";
        List<Department> mylist = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement prepStatement = conn.prepareStatement(findAllSQL)) {
            var resultSet = prepStatement.executeQuery();
            while(resultSet.next()){
                Department department = new Department();
                department.setDepartment_id(resultSet.getInt("department_id"));
                department.setName(resultSet.getString("name"));

                mylist.add(department);
            }
            return mylist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
