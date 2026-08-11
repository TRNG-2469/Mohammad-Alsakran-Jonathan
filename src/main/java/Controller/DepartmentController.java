package Controller;


import Model.Department;
import Service.DepartmentService;
import Service.DepartmentServiceImpl;
import exceptions.ErrorResponse;
import io.javalin.http.Context;

public class DepartmentController {
    private final DepartmentService departmentService = new DepartmentServiceImpl();

    public void getAllDepartments(Context ctx){
        ctx.json(departmentService.findAll());

    }
    public void getDepartmentById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Department department = departmentService.findById(id);
        if (department == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse("Department not found."));
            return;
        }
        ctx.json(department);
    }

    public void createDepartment(Context ctx){
        Department payload = ctx.bodyAsClass(Department.class);
        Department newDepartment = departmentService.create(payload);
        ctx.status(201);
        ctx.json(newDepartment);
    }


}
