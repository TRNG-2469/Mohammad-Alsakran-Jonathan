package Controller;

import Model.Reimbursement;
import Service.ReimbursementService;
import Service.ReimbursementServiceImpl;
import exceptions.ErrorResponse;
import io.javalin.http.Context;


public class ReimbursementController {
    private final ReimbursementService reimbursementService = new ReimbursementServiceImpl();

    public void createReimbursement(Context ctx){
        Reimbursement payload = ctx.bodyAsClass(Reimbursement.class);
        Reimbursement newReimbursement = reimbursementService.create(payload);
        ctx.status(201);
        ctx.json(newReimbursement);
    }

    public void getAllReimbursements(Context ctx){

        ctx.json(reimbursementService.findAll());
    }

    public void getReimbursementByID(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Reimbursement reimbursement = reimbursementService.findById(id);
        if (reimbursement == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse("Reimbursement not found."));
            return;
        }
        ctx.json(reimbursement);
    }


    public void updateReimbursement(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Reimbursement payload = ctx.bodyAsClass(Reimbursement.class);
        payload.setReimbursements_id(id);
        reimbursementService.update(payload);
        ctx.json(payload);
    }

    public void resolveReimbursement(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));

        Reimbursement payload = ctx.bodyAsClass(Reimbursement.class);
        payload.setReimbursements_id(id);
        Integer resolverId = ctx.sessionAttribute("user_id");
        payload.setResolver_id(resolverId)  ;
        reimbursementService.resolve(payload);
        ctx.json(payload);
    }

    public void getReimbursementByAuthor(Context ctx){
        int authorId = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(reimbursementService.findByAuthorId(authorId));
    }

    public void getReimbursementsByStatusAndDepartment(Context ctx){
        String status = ctx.queryParam("status");
        int departmentId = Integer.parseInt(ctx.queryParam("department_id"));
        ctx.json(reimbursementService.findAllByStatusAndDepartment(status, departmentId));
    }

}