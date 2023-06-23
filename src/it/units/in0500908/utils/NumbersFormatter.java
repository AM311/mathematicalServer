package it.units.in0500908.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Alessio Manià - IN0500908
 */
public class NumbersFormatter {
	private static final int numOfDecimals = 5;

	public static String millisFormat(long millis) {
		return millisFormat((double) millis);
	}

	public static String millisFormat(double millis) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.#", dfs);
		df.setMinimumFractionDigits(numOfDecimals);
		df.setGroupingUsed(false);

		double seconds = millis / 1000.0;
		return df.format(seconds);
	}

	public static String decimalFormat(double number) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.#", dfs);
		df.setMinimumFractionDigits(numOfDecimals);
		df.setGroupingUsed(false);

		return df.format(number);
	}

	private void decimalFormatSetup() {

	}
}
