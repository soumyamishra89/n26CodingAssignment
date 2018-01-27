/**
 * 
 */
package com.n26.assignment.entity;

/**
 * @author Soumya Mishra contains the statistics of the transaction
 */
public class Statistic {

	private double sum;

	private Double max;

	private Double min;

	private long count;

	private long timeStamp;

	/**
	 * Default Constructor
	 */
	public Statistic() {

	}

	public Statistic(double sum, Double max, Double min, long count, long timeStamp) {
		super();
		this.sum = sum;
		this.max = max;
		this.min = min;
		this.count = count;
		this.timeStamp = timeStamp;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "Statistic [sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + ", timeStamp="
				+ timeStamp + "]";
	}

}
