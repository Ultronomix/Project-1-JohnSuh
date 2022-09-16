package com.github.project1.status;

import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.reimburs.ReimbDAO;
import com.github.project1.reimburs.Reimbursements;
import com.github.project1.reimburs.UpdateReimbRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatusService {

    private static Logger logger = LogManager.getLogger(StatusService.class);

    private final ReimbDAO reimbDAO;

    public StatusService(ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    
    
    public void updateStatusAndResolver(UpdateReimbRequest updateStatusAndResolver) {

        System.out.println(updateStatusAndResolver);

        Reimbursements statusAndResolver = reimbDAO.findReimbById(updateStatusAndResolver.getReimbId()).orElseThrow(ResourceNotFoundException::new);
        
        if (updateStatusAndResolver.getResolverId() != null) {
            statusAndResolver.setResolverId(updateStatusAndResolver.getResolverId());
        }

        if (updateStatusAndResolver.getStatusId() != null) {
            statusAndResolver.setStatusId(updateStatusAndResolver.getStatusId());
        }

        reimbDAO.updateReimb(statusAndResolver);
    }
}
