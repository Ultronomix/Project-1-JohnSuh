package com.github.project1.reimburs;

import com.github.project1.common.Request;

public class NewReimbursementRequest implements Request<Reimbursements> {

    private String reimbId;
    private int amount;
    private String submitted;
    private String description;
    private String paymentId;
    private String authorId;
    private String typeId;

    public String getReimbId() {
        return reimbId;
    }

    public void setReimbId(String reimbId) {
        this.reimbId = reimbId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    

    @Override
    public String toString() {
        return "NewReimbursementRequest [amount=" + amount + ", authorId=" + authorId + ", description=" + description
                + ", paymentId=" + paymentId + ", reimbId=" + reimbId + ", submitted=" + submitted + ", typeId="
                + typeId + "]";
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractEntity = new Reimbursements();
        extractEntity.setReimbId(this.reimbId);
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPaymentId(this.paymentId);
        extractEntity.setAuthorId(this.authorId);
        extractEntity.setTypeId(this.typeId);
        return null;
    }

}
