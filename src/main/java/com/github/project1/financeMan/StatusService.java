package com.github.project1.financeMan;

import java.util.List;
import java.util.stream.Collectors;

import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.reimburs.ReimbDAO;
import com.github.project1.reimburs.ReimbResponse;
import com.github.project1.reimburs.Reimbursements;

public class StatusService {

    private final ReimbDAO reimbDAO;

    public StatusService(ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<ReimbResponse> getAllReimb() {

        return reimbDAO.getAllReimbs()
                        .stream()
                       .map(ReimbResponse::new)
                       .collect(Collectors.toList());

    }

        public ReimbResponse getReimbByStatus(String statusId) {

        if (statusId == null || statusId.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }

        try {

            return reimbDAO.findReimbByStatus(statusId)
                            .map(ReimbResponse::new)
                            .orElseThrow(ResourceNotFoundException::new);
                            
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid status id was provided.");
        }
    }

    public ReimbResponse updateReimbAdmin(UpdateStatusRequest updateReimbAdmin) {

        System.out.println(updateReimbAdmin);

        Reimbursements resolver = reimbDAO.findReimbById(updateReimbAdmin.getReimbId())
                                                   .orElseThrow(ResourceNotFoundException::new);

        if (updateReimbAdmin.getStatusId() != null) {
            resolver.setStatusId(updateReimbAdmin.getStatusId());
        }

        reimbDAO.updateStatus(resolver);
        return new ReimbResponse(resolver);
    }
}
