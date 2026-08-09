package Service;

import Model.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    Department findById(int id);
    List<Department> findAll();
}
