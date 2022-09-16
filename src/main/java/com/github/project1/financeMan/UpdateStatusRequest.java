package com.github.project1.financeMan;

public class UpdateStatusRequest {

    private String resolverId;
    private String statusId;
    private String reimbId;
    
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
    public String getReimbId() {
        return reimbId;
    }
    public void setReimbId(String reimbId) {
        this.reimbId = reimbId;
    }
    @Override
    public String toString() {
        return "UpdateStatusRequest [reimbId=" + reimbId + ", resolverId=" + resolverId + ", statusId=" + statusId
                + "]";
    }

    
 
}
