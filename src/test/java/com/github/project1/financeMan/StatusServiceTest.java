package com.github.project1.financeMan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.common.exceptions.ResourcePersistenceException;
import com.github.project1.reimburs.ReimbDAO;
import com.github.project1.reimburs.ReimbResponse;
import com.github.project1.reimburs.Reimbursements;
import com.github.project1.reimburs.UpdateReimbRequest;

public class StatusServiceTest {

    StatusService sut;
    ReimbDAO mockReimbDAO;
    UpdateStatusRequest mockUpdateStatusRequest;
    
    @BeforeEach
    public void setup() {
        mockReimbDAO = Mockito.mock(ReimbDAO.class);
        sut = new StatusService(mockReimbDAO);
        mockUpdateStatusRequest = Mockito.mock(UpdateStatusRequest.class);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockReimbDAO);
    }
    
    @Test
    public void test_getReimbByStatus_givenValidId() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now();
        Reimbursements reimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "resolverId", "pending", "food");
        when(mockReimbDAO.findReimbByStatus(anyString())).thenReturn(Optional.of(reimbStub));
        ReimbResponse expectedResult = new ReimbResponse(reimbStub);


        ReimbResponse actualResult = sut.getReimbByStatus("pending");


        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockReimbDAO, times(1)).findReimbByStatus(anyString());
    }

    @Test
    public void test_getReimbByStatus_throwsInvalidRequestException_givenNullId() {

        String idStub = null;
        

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus(idStub);
        });

        verify(mockReimbDAO, times(0)).findReimbByStatus(anyString());

    }

    @Test
    public void test_getReimbByStatus_throwsInvalidRequestException_givenUnknownReimbId() {

        String unknownStatusId = "unknown-status";
        when(mockReimbDAO.findReimbByStatus(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbByStatus(unknownStatusId);
        });

        verify(mockReimbDAO, times(1)).findReimbByStatus(anyString());

    }

    @Test
    public void test_getReimbByStatus_throwsInvalidRequestException_givenInvalidUUIDString() {

        String badStatusId = "";

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbByStatus(badStatusId);
        });

        verify(mockReimbDAO, times(0)).findReimbByStatus(anyString());
    }

    @Test
    public void test_updateReimbStatus_givenCorrectStatusId() {

        //Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now();
        Reimbursements reimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "null", "pending", "food");
        Reimbursements updatedReimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "null", "denied", "food");
        when(mockReimbDAO.findReimbById(anyString())).thenReturn(Optional.of(reimbStub));

        doAnswer((i) -> {
		
			assertTrue("denied".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateStatusRequest).setStatusId(anyString());

        doAnswer((i) -> {
		
			assertTrue("some-uuid".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateStatusRequest).setReimbId(anyString());
        
        when(mockUpdateStatusRequest.getStatusId()).thenReturn("denied");
        when(mockUpdateStatusRequest.getReimbId()).thenReturn("some-uuid");
        

        ReimbResponse expectedResult = new ReimbResponse(updatedReimbStub);

        //Act
        ReimbResponse actualResult = sut.updateReimbAdmin(mockUpdateStatusRequest);
       
        //Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockReimbDAO, times(1)).updateStatus(any(Reimbursements.class));
    }

    


}