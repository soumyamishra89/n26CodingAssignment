/**
 * 
 */
package com.n26.assignment.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.n26.assignment.entity.OverallStatistics;
import com.n26.assignment.entity.Transaction;
import com.n26.assignment.services.AssignmentService;

/**
 * @author Soumya Mishra
 *         <p>
 *         Test class for testing functionalities of
 *         {@link AssignmentController}
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AssignmentControllerTests {
	private final Logger logger = LoggerFactory.getLogger(AssignmentControllerTests.class);

	@Mock
	private AssignmentService assignmentService;

	@InjectMocks
	private AssignmentController assignmentController;

	@Test
	public void testGetOverallStatistics() {
		ResponseEntity<OverallStatistics> responseEntity = assignmentController.getStatistics();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Mockito.verify(assignmentService).getOverallStatistics();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testPostTransactionWhenTransactionIsNull() {

		ResponseEntity responseEntity = assignmentController.postTransaction(null);
		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		Mockito.verifyZeroInteractions(assignmentService);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testPostTransactionWhenTransactionIsValid() {
		Transaction transaction = new Transaction(45.7, System.currentTimeMillis());
		Mockito.when(assignmentService.postTransaction(transaction)).thenReturn(HttpStatus.CREATED);
		ResponseEntity responseEntity = assignmentController.postTransaction(transaction);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		Mockito.verify(assignmentService).postTransaction(transaction);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testPostTransactionWhenTransactionIsInValid() {

		Transaction transaction = new Transaction(45.7, System.currentTimeMillis() - 61000l);
		Mockito.when(assignmentService.postTransaction(transaction)).thenReturn(HttpStatus.NO_CONTENT);

		ResponseEntity responseEntity = assignmentController.postTransaction(transaction);
		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		Mockito.verify(assignmentService).postTransaction(transaction);
	}
}
