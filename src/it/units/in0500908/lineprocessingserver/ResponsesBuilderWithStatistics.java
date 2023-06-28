package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public abstract class ResponsesBuilderWithStatistics implements ResponsesBuilder {
	protected int responsesCounter = 0;				//le risposte contate dipendono dall'implementazione scelta!
	protected int sumOfResponseTime = 0;           	//in ms
	protected int maxResponseTime = 0;              //in ms

	public int getResponsesCounter() {
		return responsesCounter;
	}

	public int getSumOfResponseTime() {
		return sumOfResponseTime;
	}

	public double getAvgResponseTime() {
		return (double) sumOfResponseTime / responsesCounter;
	}

	public int getMaxResponseTime() {
		return maxResponseTime;
	}

	//=======================

	public abstract String buildOkResponse(String message, long startingMillis);
	public abstract String buildErrResponse(String message, long startingMillis);

	protected synchronized void updateCounters(int responseTime) {
		responsesCounter++;
		sumOfResponseTime += responseTime;
		maxResponseTime = Math.max(maxResponseTime, responseTime);
	}

}
