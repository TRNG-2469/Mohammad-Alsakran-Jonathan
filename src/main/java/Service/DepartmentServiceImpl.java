package Service;

import Dao.DepartmentDAO;
import Dao.DepartmentDAOImpl;
import Model.Department;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDAO departmentDAO = new DepartmentDAOImpl();

    @Override
    public Department create(Department department) {
        if (department == null || department.getName() == null || department.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        return departmentDAO.create(department);
    }

    @Override
    public Department findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid department ID");
        }
        return departmentDAO.findById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }
}
