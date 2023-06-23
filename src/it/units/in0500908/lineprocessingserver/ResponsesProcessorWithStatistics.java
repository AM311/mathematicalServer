package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public abstract class ResponsesProcessorWithStatistics extends ResponsesProcessor {
	protected int okResponsesCounter = 0;
	protected int sumOfResponseTime = 0;			//in ms
	protected int maxResponseTime = 0;				//in ms

	public int getOkResponsesCounter() {
		return okResponsesCounter;
	}
	public int getSumOfResponseTime() {
		return sumOfResponseTime;
	}
	public float getAvgResponseTime() {
		return (float) sumOfResponseTime /okResponsesCounter;
	}
	public int getMaxResponseTime() {
		return maxResponseTime;
	}

	public ResponsesProcessorWithStatistics(String okResponsesMessage, String errResponsesMessage) {
		super(okResponsesMessage, errResponsesMessage);
	}

	//=======================

	/**
	 * Do not use with statistics processor.
	 * @param message
	 * @return
	 */
	@Override
	public String buildOkResponse(String message) {
		return buildOkResponse(message, System.currentTimeMillis());
	}

	/**
	 * Any implementation should call inside the <code>updateCounters</code> private method.
	 * @param message
	 * @param startingMillis
	 * @return
	 */
	public abstract String buildOkResponse(String message, long startingMillis);

	protected void updateCounters(int responseTime) {
		okResponsesCounter++;
		sumOfResponseTime += responseTime;
		maxResponseTime = Math.max(maxResponseTime, responseTime);
	}

}
