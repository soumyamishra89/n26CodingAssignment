/**
 * 
 */
package com.n26.assignment.services;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		transaction = new Transaction(50.8, System.currentTimeMillis() - 10000l);
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(21.3, System.currentTimeMillis() - 2);
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(200.47, System.currentTimeMillis());
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);
		transaction = new Transaction(129.42, System.currentTimeMillis());
		httpStatus = assignmentService.postTransaction(transaction);
		assertEquals(httpStatus, HttpStatus.CREATED);

		OverallStatistics overallStatistics = assignmentService.getOverallStatistics();
		assertEquals(90.44, overallStatistics.getAvg(), 0.0);
		assertEquals(452.19, overallStatistics.getSum(), 0.0);
		assertEquals(200.47, overallStatistics.getMax(), 0.0);
		assertEquals(21.3, overallStatistics.getMin(), 0.0);
		assertEquals(5, overallStatistics.getCount());
	}

	@Test
	public void testMultipleTransactionsWithOverallStatistics() throws InterruptedException {
		long currentTime = System.currentTimeMillis();
		List<Transaction> transactions = new ArrayList<>(30);
		double sum = 0;

		for (int i = 0; i < 30; i++) {
			if (i < 10) {
				transactions.add(new Transaction(i + 20.9, currentTime));
				sum += (i + 20.9);
			} else {
				transactions.add(new Transaction(i + 76.5, currentTime - (i * 1000)));
				sum += (i + 76.5);
			}
		}
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		for (final Transaction transaction : transactions) {
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					assignmentService.postTransaction(transaction);

				}
			});
		}
		// delay to wait for the transactions to complete
		Thread.sleep(10000);
		OverallStatistics overallStatistics = assignmentService.getOverallStatistics();
		assertEquals(Double.valueOf(new DecimalFormat("###.##").format(sum / 30)), overallStatistics.getAvg(), 0.0);
		assertEquals(sum, overallStatistics.getSum(), 0.0);
		assertEquals(105.5, overallStatistics.getMax(), 0.0);
		assertEquals(20.9, overallStatistics.getMin(), 0.0);
		assertEquals(30, overallStatistics.getCount());

	}
}
