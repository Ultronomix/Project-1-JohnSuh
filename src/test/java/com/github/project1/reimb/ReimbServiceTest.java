package com.github.project1.reimb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
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
import com.github.project1.reimburs.ReimbService;
import com.github.project1.reimburs.Reimbursements;
import com.github.project1.reimburs.UpdateReimbRequest;

public class ReimbServiceTest {

    ReimbService sut;
    ReimbDAO mockReimbDAO;
    UpdateReimbRequest mockUpdateReimbRequest;
    
    @BeforeEach
    public void setup() {
        mockReimbDAO = Mockito.mock(ReimbDAO.class);
        sut = new ReimbService(mockReimbDAO);
        mockUpdateReimbRequest = Mockito.mock(UpdateReimbRequest.class);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockReimbDAO);
    }

    @Test
    public void test_getAllReimbsById() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now();
        List<Reimbursements> mockList = new ArrayList<>();
        Reimbursements reimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "resolverId", "pending", "food");
        Reimbursements reimbStub2 = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "resolverId", "pending", "food");
        mockList.add(reimbStub);
        mockList.add(reimbStub2);
        when(mockReimbDAO.getAllReimbs()).thenReturn(mockList);
        List<ReimbResponse> expectedResult = mockReimbDAO.getAllReimbs().stream().map(ReimbResponse::new).collect(Collectors.toList());
       
        List<ReimbResponse> actualResult = sut.getAllReimb();

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);  // The objects compared need a .equals method in DAO
        verify(mockReimbDAO, times(2)).getAllReimbs();
    }

    @Test
    public void test_getReimbById_givenValidId() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now();
        Reimbursements reimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "resolverId", "pending", "food");
        when(mockReimbDAO.findReimbById(anyString())).thenReturn(Optional.of(reimbStub));
        ReimbResponse expectedResult = new ReimbResponse(reimbStub);


        ReimbResponse actualResult = sut.getReimbById("some-uuid");


        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockReimbDAO, times(1)).findReimbById(anyString());
    }

    @Test
    public void test_getReimbById_throwsInvalidRequestException_givenNullId() {

        String idStub = null;
        

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbById(idStub);
        });

        verify(mockReimbDAO, times(0)).findReimbById(anyString());

    }

    @Test
    public void test_getReimbById_throwsInvalidRequestException_givenUnknownReimbId() {

        String unknownReimbId = "unknown-uuid";
        when(mockReimbDAO.findReimbById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbById(unknownReimbId);
        });

        verify(mockReimbDAO, times(1)).findReimbById(anyString());

    }

    @Test
    public void test_getReimbById_throwsInvalidRequestException_givenInvalidUUIDString() {

        String badReimbId = "";

        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbById(badReimbId);
        });

        verify(mockReimbDAO, times(0)).findReimbById(anyString());
    }


    @Test
    public void test_updateUser_GivenValidUserToUpdate() {
        
        //Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now();
        Reimbursements reimbStub = new Reimbursements("some-uuid", 1, now, then, "description", "userId", "resolverId", "pending", "food");
        Reimbursements updatedReimbStub = new Reimbursements("some-uuid", 1, now, then, "new description", "userId", "resolverId", "pending", "travel");
        when(mockReimbDAO.findReimbById(anyString())).thenReturn(Optional.of(reimbStub));

        doAnswer((i) -> {
		
			assertTrue("new description".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateReimbRequest).setDescription(anyString());

        doAnswer((i) -> {
		
			assertTrue("travel".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateReimbRequest).setTypeId(anyString());

        doAnswer((i) -> {
			
			assertTrue("some-uuid".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateReimbRequest).setAuthorId(anyString());
 
        when(mockUpdateReimbRequest.getDescription()).thenReturn("new description");
        when(mockUpdateReimbRequest.getTypeId()).thenReturn("travel");
        when(mockUpdateReimbRequest.getReimbId()).thenReturn("some-uuid");

        ReimbResponse expectedResult = new ReimbResponse(updatedReimbStub);

        //Act
        ReimbResponse actualResult = sut.updateReimb(mockUpdateReimbRequest);
       
        //Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockReimbDAO, times(1)).updateReimb(any(Reimbursements.class));

    }

    
    
}
