package Controller;


import Model.Department;
import Service.DepartmentService;
import Service.DepartmentServiceImpl;
import io.javalin.http.Context;

public class DepartmentController {
    private final DepartmentService departmentService = new DepartmentServiceImpl();

    public void getAllDepartments(Context ctx){
        ctx.json(departmentService.findAll());

    }
    public void getDepartmentById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(departmentService.findById(id));
    }

    public void createDepartment(Context ctx){
        Department payload = ctx.bodyAsClass(Department.class);
        Department newDepartment = departmentService.create(payload);
        ctx.status(201);
        ctx.json(newDepartment);
    }


}
