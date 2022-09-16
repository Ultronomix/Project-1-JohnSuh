package com.github.project1.financeMan;

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

    public void updateReimbAdmin(UpdateStatusRequest updateReimbAdmin) {

        System.out.println(updateReimbAdmin);

        Reimbursements resolver = reimbDAO.findReimbById(updateReimbAdmin.getReimbId())
                                                   .orElseThrow(ResourceNotFoundException::new);

        if (updateReimbAdmin.getStatusId() != null) {
            resolver.setStatusId(updateReimbAdmin.getStatusId());
        }

        reimbDAO.updateStatus(resolver);
    }
}
