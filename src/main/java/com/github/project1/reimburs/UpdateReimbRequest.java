package com.github.project1.reimburs;

public class UpdateReimbRequest {

    private String reimbId;
    private int amount;
    private String resolved;
    private String resolverId;
    private String statusId;
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

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + amount;
        result = prime * result + ((reimbId == null) ? 0 : reimbId.hashCode());
        result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
        result = prime * result + ((resolverId == null) ? 0 : resolverId.hashCode());
        result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UpdateReimbRequest other = (UpdateReimbRequest) obj;
        if (amount != other.amount)
            return false;
        if (reimbId == null) {
            if (other.reimbId != null)
                return false;
        } else if (!reimbId.equals(other.reimbId))
            return false;
        if (resolved == null) {
            if (other.resolved != null)
                return false;
        } else if (!resolved.equals(other.resolved))
            return false;
        if (resolverId == null) {
            if (other.resolverId != null)
                return false;
        } else if (!resolverId.equals(other.resolverId))
            return false;
        if (statusId == null) {
            if (other.statusId != null)
                return false;
        } else if (!statusId.equals(other.statusId))
            return false;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UpdateReimbRequest [amount=" + amount + ", reimbId=" + reimbId + ", resolved=" + resolved
                + ", resolverId=" + resolverId + ", statusId=" + statusId + ", typeId=" + typeId + "]";
    }

}
