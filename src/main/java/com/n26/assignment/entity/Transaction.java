/**
 * 
 */
package com.n26.assignment.entity;

/**
 * @author Soumya Mishra Contains the amount and timestamp from transaction rest
 *         api call
 */
public class Transaction {

	private double amount;

	private long timestamp;

	/**
	 * Default Constructor
	 */
	public Transaction() {
	}

	public Transaction(double amount, long timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public double getAmount() {
		return amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
