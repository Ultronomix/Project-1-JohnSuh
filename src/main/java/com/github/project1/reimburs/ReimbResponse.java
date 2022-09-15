package com.github.project1.reimburs;

import java.io.Serializable;

public class ReimbResponse implements Serializable {

    private String reimbId;
    private float amount;
    private String submitted;
    private String resolved;
    private String description;
    private String paymentId;
    private String authorId;
    private String resolverId;
    private String statusId;
    private String typeId;

    public ReimbResponse (Reimbursements subject) {
        this.reimbId = subject.getReimbId();
        this.amount = subject.getAmount();
        this.submitted = subject.getSubmitted().toString();
        this.resolved = subject.getResolved().toString();
        this.description = subject.getDescription();
        this.paymentId = subject.getPaymentId();
        this.authorId = subject.getAuthorId();
        this.resolverId = subject.getResolverId();
        this.statusId = subject.getStatusId();
        this.typeId = subject.getTypeId();
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(amount);
        result = prime * result + ((authorId == null) ? 0 : authorId.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
        result = prime * result + ((reimbId == null) ? 0 : reimbId.hashCode());
        result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
        result = prime * result + ((resolverId == null) ? 0 : resolverId.hashCode());
        result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
        result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
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
        ReimbResponse other = (ReimbResponse) obj;
        if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount))
            return false;
        if (authorId == null) {
            if (other.authorId != null)
                return false;
        } else if (!authorId.equals(other.authorId))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (paymentId == null) {
            if (other.paymentId != null)
                return false;
        } else if (!paymentId.equals(other.paymentId))
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
        if (submitted == null) {
            if (other.submitted != null)
                return false;
        } else if (!submitted.equals(other.submitted))
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
        return "ReimbResponse [amount=" + amount + ", authorId=" + authorId + ", description=" + description
                + ", paymentId=" + paymentId + ", reimbId=" + reimbId + ", resolved=" + resolved + ", resolverId="
                + resolverId + ", statusId=" + statusId + ", submitted=" + submitted + ", typeId=" + typeId + "]";
    }

    
    
}
