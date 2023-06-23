package it.units.in0500908.utils;

import java.io.PrintStream;

/**
 * @author Alessio Manià - IN0500908
 */
public class Logger {
	//Non-instantiable class
	private Logger() {}
	//---------------------------
	public static void printLog(PrintStream ps, String message) {
		ps.printf("[%1$tY-%1$tm-%1$td %1$tT] %2$s %n", System.currentTimeMillis(), message);
	}
}
