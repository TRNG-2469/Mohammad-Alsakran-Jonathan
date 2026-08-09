package Dao;

import Model.Department;


import java.util.List;

public interface DepartmentDAO {

    Department create(Department department);
    Department findById(int id);
    List<Department> findAll();
}


