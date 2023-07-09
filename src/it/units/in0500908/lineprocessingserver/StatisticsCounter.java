package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public class StatisticsCounter {
	protected int responsesCounter = 0;
	protected int sumOfResponseTime = 0;            //in ms
	protected int maxResponseTime = 0;              //in ms

	//--------------

	public synchronized int getNumOfResponses() {
		return responsesCounter;
	}

	public synchronized double getAvgResponseTime() {
		return (double) sumOfResponseTime / responsesCounter;
	}

	public synchronized int getMaxResponseTime() {
		return maxResponseTime;
	}

	//--------------

	public synchronized void updateStatistics(int responseTime) {
		responsesCounter++;
		sumOfResponseTime += responseTime;
		maxResponseTime = Math.max(maxResponseTime, responseTime);
	}
}
