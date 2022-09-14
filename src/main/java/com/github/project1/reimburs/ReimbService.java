package com.github.project1.reimburs;

import com.github.project1.common.ResourceCreationResponse;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class ReimbService {

    private final ReimbDAO reimbDAO;

    public ReimbService (ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<ReimbResponse> getAllReimb() {

        return reimbDAO.getAllReimbs().stream()
                       .map(ReimbResponse::new)
                       .collect(Collectors.toList());
    }

    public ReimbResponse getReimbById(String reimbId) {
        if (reimbId == null || reimbId.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }

        try {
            return reimbDAO.findReimbById(reimbId)
                            .map(ReimbResponse::new)
                            .orElseThrow(ResourceNotFoundException::new);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid UUID string was provided.");
        }

    }

    public void updateReimb(UpdateReimbRequest updateReimbRequest) {
        System.out.println(updateReimbRequest);

        Reimbursements reimbToUpdate = reimbDAO.findReimbById(updateReimbRequest.getReimbId()).orElseThrow(ResourceNotFoundException::new);
    
        if (updateReimbRequest.getAmount() != 0) {
            reimbToUpdate.setAmount(reimbToUpdate.getAmount());

        }

        if (updateReimbRequest.getResolved() != null) {
            reimbToUpdate.setResolved(reimbToUpdate.getResolved());

        }

        if (updateReimbRequest.getResolverId() != null) {
            reimbToUpdate.setResolverId(reimbToUpdate.getResolverId());

        }

        if (updateReimbRequest.getStatusId() != null) {
            reimbToUpdate.setStatusId(reimbToUpdate.getStatusId());

        }

        if (updateReimbRequest.getTypeId() != null) {
            reimbToUpdate.setTypeId(reimbToUpdate.getTypeId());
        }

        reimbDAO.updateReimb(reimbToUpdate);

    }

    public ResourceCreationResponse create(NewReimbursementRequest newReimb) {

        if (newReimb == null) {
            throw new InvalidRequestException("Provided request payload was null.");

        }

        if (newReimb.getAmount() == 0) {
            throw new InvalidRequestException("Provided request payload was null.");
       
        }

        if (newReimb.getTypeId() == null){

        }

        Reimbursements reimbToPersist = newReimb.extractEntity();
        String newReimbId = reimbDAO.save(reimbToPersist);
        return new ResourceCreationResponse(newReimbId);
    }
    
}
