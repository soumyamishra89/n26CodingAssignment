/**
 * 
 */
package com.n26.assignment.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.n26.assignment.entity.OverallStatistics;
import com.n26.assignment.entity.Statistic;
import com.n26.assignment.entity.Transaction;
import com.n26.assignment.utils.Utils;

/**
 * @author Soumya Mishra
 *         <p>
 *         This service class contains the service for adding a transaction and
 *         getting the statistics
 *
 */
@Service
public class AssignmentService {

	private final Logger logger = LoggerFactory.getLogger(AssignmentService.class);
	private final OverallStatistics overallstat = new OverallStatistics(0, 0, Double.MIN_VALUE, Double.MAX_VALUE, 0);

	private List<Statistic> statisticList = new ArrayList<>(60);

	private DecimalFormat decimalFormatter = new DecimalFormat("###.##");

	@PostConstruct
	public void initialise() {
		logger.info("Initiliasing statistics list");
		Calendar calendar = Utils.getCurrentTimeWithoutMillis();
		for (int i = 0; i < 60; i++) {
			calendar.set(Calendar.SECOND, i);

			statisticList.add(i, new Statistic(0, Double.MIN_VALUE, Double.MAX_VALUE, 0, calendar.getTimeInMillis()));
		}
	}

	/**
	 * 
	 * @param transaction
	 *            to be added
	 * @return {@link HttpStatus#CREATED} if the transaction is added else
	 *         {@link HttpStatus#NO_CONTENT}
	 */
	public HttpStatus postTransaction(Transaction transaction) {
		logger.info("postTransaction: Start");
		HttpStatus httpStatus = HttpStatus.NO_CONTENT;
		synchronized (overallstat) {
			if (Utils.isTransactionValid(transaction.getTimestamp())) {
				Calendar transactionTime = Utils.getTimeFromTimeStampWithoutMillis(transaction.getTimestamp());
				int second = transactionTime.get(Calendar.SECOND);
				Statistic stat = statisticList.get(second);
				if (stat.getTimeStamp() == transactionTime.getTimeInMillis()) {
					updateStatistic(stat, transaction);
					updateOverallStatistics(transaction);
				} else {
					updateStatisticsToNewTransaction(stat, transaction, transactionTime);

					updateOverallStatistics();
				}
				httpStatus = HttpStatus.CREATED;

			}
		}
		logger.info("postTransaction: End" + overallstat.toString());
		return httpStatus;
	}

	/**
	 * 
	 * @return the overall statistics
	 */
	public OverallStatistics getOverallStatistics() {
		logger.info("getOverallStatistics");
		return overallstat;
	}

	/**
	 * Schedules the removal of a statistic corresponding to t-61 second
	 */
	@Scheduled(fixedDelay = 1000)
	public void updateStatistics() {
		logger.info("updateStatistics: Start");
		synchronized (overallstat) {
			Calendar currentTime = Utils.getCurrentTimeWithoutMillis();
			Statistic stat = statisticList.get(currentTime.get(Calendar.SECOND));
			Calendar statisticTime = Utils.getTimeFromTimeStampWithoutMillis(stat.getTimeStamp());
			if (statisticTime.compareTo(currentTime) != 0) {
				resetStatistcis(stat, currentTime);
			}
		}
		logger.info("updateStatistics: End");
	}

	/**
	 * Resets the statistics to initial values
	 */
	private void resetStatistcis(Statistic stat, Calendar currentTime) {
		stat.setCount(0);
		stat.setMax(Double.MIN_VALUE);
		stat.setMin(Double.MAX_VALUE);
		stat.setSum(0.0);
		stat.setTimeStamp(currentTime.getTimeInMillis());

	}

	/**
	 * 
	 * resets the statistics and updates it with the new values from transaction
	 */
	private void updateStatisticsToNewTransaction(Statistic stat, Transaction transaction, Calendar transactionTime) {
		stat.setCount(1);
		stat.setMax(transaction.getAmount());
		stat.setMin(transaction.getAmount());
		stat.setSum(transaction.getAmount());
		stat.setTimeStamp(transactionTime.getTimeInMillis());

	}

	/**
	 * 
	 * Updates the statistic corresponding to a particular second of a time
	 */
	private void updateStatistic(Statistic stat, Transaction transaction) {
		stat.setSum(stat.getSum() + transaction.getAmount());
		double max = stat.getMax();
		double min = stat.getMin();
		stat.setMax(transaction.getAmount() > max ? transaction.getAmount() : max);
		stat.setMin(transaction.getAmount() < min ? transaction.getAmount() : min);
		stat.setCount(stat.getCount() + 1);
	}

	/**
	 * Updates the overall statistics when a new transaction arrives with the
	 * transaction details
	 */
	private void updateOverallStatistics(Transaction transaction) {
		overallstat.setSum(overallstat.getSum() + transaction.getAmount());
		double max = overallstat.getMax();
		double min = overallstat.getMin();
		overallstat.setMax(transaction.getAmount() > max ? transaction.getAmount() : max);
		overallstat.setMin(transaction.getAmount() < min ? transaction.getAmount() : min);
		overallstat.setCount(overallstat.getCount() + 1);
		overallstat.setAvg(Double.valueOf(decimalFormatter.format(overallstat.getSum() / overallstat.getCount())));

	}

	private void updateOverallStatistics() {

		overallstat.setSum(0);
		overallstat.setMax(Double.MIN_VALUE);
		overallstat.setMin(Double.MAX_VALUE);
		overallstat.setCount(0);
		statisticList.forEach(stat -> {
			overallstat.setSum(overallstat.getSum() + stat.getSum());
			overallstat.setMax(stat.getMax() > overallstat.getMax() ? stat.getMax() : overallstat.getMax());
			overallstat.setMin(stat.getMin() < overallstat.getMin() ? stat.getMin() : overallstat.getMin());
			overallstat.setCount(overallstat.getCount() + stat.getCount());
		});
		overallstat.setAvg(Double.valueOf(decimalFormatter.format(overallstat.getSum() / overallstat.getCount())));
	}
}
