package Service;

import Dao.ReimbursementDAO;
import Dao.ReimbursementDAOImpl;
import Model.Reimbursement;

import java.math.BigDecimal;
import java.util.List;

public class ReimbursementServiceImpl implements ReimbursementService {

    private final ReimbursementDAO reimbursementDAO = new ReimbursementDAOImpl();

    @Override
    public Reimbursement create(Reimbursement reimbursement) {
        if (reimbursement.getAmount() == null || reimbursement.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (reimbursement.getDescription() == null || reimbursement.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        String type = reimbursement.getType();
        if (type == null || !(type.equals("TRAVEL") || type.equals("FOOD") || type.equals("LODGING") || type.equals("OTHER"))) {
            throw new IllegalArgumentException("Type must be one of: TRAVEL, FOOD, LODGING, OTHER.");
        }

        reimbursement.setStatus("PENDING");
        reimbursement.setResolver_id(null);

        return reimbursementDAO.create(reimbursement);
    }


    @Override
    public Reimbursement findById(int id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be <= 0");
        }
        return reimbursementDAO.findById(id);
    }

    @Override
    public void update(Reimbursement reimbursement) {
        Reimbursement existing = reimbursementDAO.findById(reimbursement.getReimbursements_id());

        if (existing == null) {
            throw new IllegalArgumentException("Reimbursement not found.");
        }
        if (!existing.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("Only PENDING reimbursements can be edited.");
        }

        if (reimbursement.getAmount() == null || reimbursement.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (reimbursement.getDescription() == null || reimbursement.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        String type = reimbursement.getType();
        if (type == null || !(type.equals("TRAVEL") || type.equals("FOOD") || type.equals("LODGING") || type.equals("OTHER"))) {
            throw new IllegalArgumentException("Type must be one of: TRAVEL, FOOD, LODGING, OTHER.");
        }

        reimbursementDAO.update(reimbursement);
    }

    @Override
    public List<Reimbursement> findByAuthorId(int authorId) {
        if (authorId <= 0) {
            throw new IllegalArgumentException("Author ID cannot be <= 0");
        }
        return reimbursementDAO.findByAuthorId(authorId);
    }

    @Override
    public List<Reimbursement> findAll() {

        return reimbursementDAO.findAll();
    }

    @Override
    public void resolve(Reimbursement reimbursement) {
        Reimbursement existing = reimbursementDAO.findById(reimbursement.getReimbursements_id());

        if (existing == null) {
            throw new IllegalArgumentException("Reimbursement not found.");
        }
        if (!existing.getStatus().equals("PENDING")) {
            throw new IllegalArgumentException("Only PENDING reimbursements can be resolved.");
        }
        String status = reimbursement.getStatus();
        if (status == null || !(status.equals("APPROVED") || status.equals("DENIED"))) {
            throw new IllegalArgumentException("Status must be either APPROVED or DENIED.");
        }
        if (reimbursement.getResolver_id() == null || reimbursement.getResolver_id() <= 0) {
            throw new IllegalArgumentException("Resolver ID must be a valid positive integer.");
        }

        reimbursementDAO.resolve(reimbursement);
    }

    @Override
    public List<Reimbursement> findAllByStatusAndDepartment(String status, int departmentId) {
        if (status == null || !(status.equals("PENDING") || status.equals("APPROVED") || status.equals("DENIED"))) {
            throw new IllegalArgumentException("Status must be one of: PENDING, APPROVED, DENIED.");
        }
        if (departmentId <= 0) {
            throw new IllegalArgumentException("Department ID cannot be <= 0");
        }
        return reimbursementDAO.findAllByStatusAndDepartment(status, departmentId);
    }
}
