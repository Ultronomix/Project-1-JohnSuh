package com.github.project1.status;

import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.reimburs.ReimbDAO;
import com.github.project1.reimburs.Reimbursements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatusService {

    private static Logger logger = LogManager.getLogger(StatusService.class);

    private final ReimbDAO reimbDAO;

    public StatusService(ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public void updateStatusAndResolver(UpdateStatusRequest updateStatusAndResolver) {

        System.out.println(updateStatusAndResolver);

        Reimbursements statusAndResolver = reimbDAO.findReimbById(updateStatusAndResolver.getReimbId())
                                                   .orElseThrow(ResourceNotFoundException::new);

        if (updateStatusAndResolver.getStatusId() != null) {
            statusAndResolver.setStatusId(updateStatusAndResolver.getStatusId());
        }

        reimbDAO.updateStatus(statusAndResolver);
    }
}
