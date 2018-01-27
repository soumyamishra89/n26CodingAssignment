/**
 * 
 */
package com.n26.assignment.utils;

import java.time.Clock;
import java.util.Calendar;

/**
 * @author Soumya Mishra Utility Class for various utility function
 *
 */
public class Utils {

	/**
	 * 
	 * @return true if the transaction is within the last 60 seconds
	 */
	public static boolean isTransactionValid(long timeStamp) {
		Clock currentTime = Clock.systemUTC();
		long time = currentTime.millis() - timeStamp;
		return time >= 0 && time <= 60000;
	}

	/**
	 * 
	 * @param timeStamp
	 *            UTC time
	 * @return Calendar corresponding to the time in millis
	 */
	public static Calendar getTime(long timeStamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		return calendar;
	}

	/**
	 * 
	 * @param timeStamp
	 *            time in millis
	 * @return returns a Calendar corresponding to the timeStamp
	 */
	public static Calendar getTimeFromTimeStampWithoutMillis(long timeStamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	/**
	 * 
	 * @return The current time without the millisecond
	 */
	public static Calendar getCurrentTimeWithoutMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
