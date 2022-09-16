package com.github.project1.reimburs;

public class UpdateReimbRequest {

    private String reimbId;
    private float amount;
    private String description;
    private String typeId;

    

    public String getReimbId() {
        return reimbId;
    }

    public void setReimbId(String reimbId) {
        this.reimbId = reimbId;
    }

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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Reimbursements extractEntity() {

        Reimbursements extractedEntity = new Reimbursements();
        extractedEntity.setAmount(this.amount);
        extractedEntity.setDescription(this.description);
        extractedEntity.setTypeId(this.typeId);
        return extractedEntity;

    }

    @Override
    public String toString() {
        return "UpdateReimbRequest [amount=" + amount + ", description=" + description + ", reimbId=" + reimbId
                + ", typeId=" + typeId + "]";
    }

    

}
