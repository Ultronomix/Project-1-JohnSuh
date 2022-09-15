package com.github.project1.reimburs;

import com.github.project1.common.Request;

public class NewReimbursementRequest implements Request<Reimbursements> {

    private float amount;
    private String description;
    private String authorId;
    private String typeId;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                + ", typeId=" + typeId + "]";
    }

    @Override
    public Reimbursements extractEntity() {
        Reimbursements extractedEntity = new Reimbursements();
        extractedEntity.setAmount(this.amount);
        extractedEntity.setDescription(this.description);
        extractedEntity.setAuthorId(this.authorId);
        extractedEntity.setTypeId(this.typeId);
        return extractedEntity;
    }

}
