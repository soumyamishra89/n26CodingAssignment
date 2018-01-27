/**
 * 
 */
package com.n26.assignment.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.n26.assignment.entity.OverallStatistics;
import com.n26.assignment.entity.Transaction;

/**
 * @author Soumya Mishra
 *         <p>
 *         Test class for testing the functionality in {@link AssignmentService}
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTests {

	@InjectMocks
	private AssignmentService assignmentService;

	@Before
	public void setup() {
		assignmentService.initialise();
	}

	@Test
	public void testTransactionPersistWhenTimeIsValid() {
		Transaction transaction = new Transaction(29.8, System.currentTimeMillis());
		HttpStatus httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);

	}

	@Test
	public void testTransactionDiscardedWhenTimeIsNotValid() {
		Transaction transaction = new Transaction(29.8, System.currentTimeMillis() - 60500l);
		HttpStatus httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(HttpStatus.NO_CONTENT, httpStatus);
		transaction = new Transaction(29.8, System.currentTimeMillis() + 2000l);
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(HttpStatus.NO_CONTENT, httpStatus);
	}

	@Test
	public void testOverallStatisticsAsEmptyWhenThereAreNoTransactions() {
		OverallStatistics overallStatistics = assignmentService.getOverallStatistics();
		assertEquals(0.0, overallStatistics.getAvg(), 0.0);
		assertEquals(0.0, overallStatistics.getSum(), 0.0);
		assertEquals(Double.MIN_VALUE, overallStatistics.getMax(), 0.0);
		assertEquals(Double.MAX_VALUE, overallStatistics.getMin(), 0.0);
		assertEquals(0, overallStatistics.getCount());
	}

	@Test
	public void testOverallStatisticsWhenTransactionsArePresent() {
		Transaction transaction = new Transaction(50.2, System.currentTimeMillis());
		HttpStatus httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(29.8, System.currentTimeMillis() - 10000l);
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(29.8, System.currentTimeMillis() - 2);
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(29.8, System.currentTimeMillis());
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(29.8, System.currentTimeMillis());
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
	}
}