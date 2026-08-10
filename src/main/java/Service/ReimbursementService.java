package Service;

import Model.Reimbursement;

import java.util.List;

public interface ReimbursementService {
    Reimbursement create(Reimbursement reimbursement);
    Reimbursement findById(int id);
    void update(Reimbursement reimbursement);
    List<Reimbursement> findByAuthorId(int authorId);
    List<Reimbursement> findAll();
    void resolve(Reimbursement reimbursement);
    List<Reimbursement> findAllByStatusAndDepartment(String status, int departmentId);

}
