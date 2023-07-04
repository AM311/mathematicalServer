package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public abstract class ResponsesBuilderWithStatistics implements ResponsesBuilder {
	StatisticsCounter statisticsCounter;

	public ResponsesBuilderWithStatistics() {
		this.statisticsCounter = new StatisticsCounter();
	}

	public StatisticsCounter getStatisticsManager() {
		return statisticsCounter;
	}

	//=======================

	public abstract String buildOkResponse(String message, long startingMillis);
	public abstract String buildErrResponse(String message, long startingMillis);

}
