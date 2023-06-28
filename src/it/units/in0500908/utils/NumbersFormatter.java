package it.units.in0500908.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Alessio Manià - IN0500908
 */
public class NumbersFormatter {

	private static final int numOfDecimals = 5;					//Classe di utilità locale: numero fisso e stabilito qui

	public static String millisFormat(long millis) {
		return millisFormat((double) millis);
	}

	public static String millisFormat(double millis) {
		return decimalFormat(millis / 1000.0);
	}

	public static String decimalFormat(double number) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setInfinity("Infinity");
		DecimalFormat df = new DecimalFormat("#.#", dfs);
		df.setMinimumFractionDigits(numOfDecimals);
		df.setGroupingUsed(false);

		return df.format(number);
	}
}
