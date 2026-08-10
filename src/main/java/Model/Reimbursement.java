package Model;

import java.math.BigDecimal;

public class Reimbursement {
    private int reimbursements_id;
    private String status;
    private BigDecimal amount;
    private String description;
    private String type;
    private Integer resolver_id;
    private int author_id;

    public Reimbursement() {
    }

    public Reimbursement(int reimbursements_id, String status, BigDecimal amount, String description, String type, Integer resolver_id, int author_id) {
        this.reimbursements_id = reimbursements_id;
        this.status = status;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.resolver_id = resolver_id;
        this.author_id = author_id;
    }

    public int getReimbursements_id() {
        return reimbursements_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(Integer resolver_id) {
        this.resolver_id = resolver_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbursements_id=" + reimbursements_id +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", resolver_id=" + resolver_id +
                ", author_id=" + author_id +
                '}';
    }

    public void setReimbursements_id(int reimbursements_id) {
        this.reimbursements_id = reimbursements_id;
    }


}
