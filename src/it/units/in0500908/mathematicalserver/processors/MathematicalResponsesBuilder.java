package it.units.in0500908.mathematicalserver.processors;

import it.units.in0500908.lineprocessingserver.ResponsesBuilderWithStatistics;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalResponsesBuilder extends ResponsesBuilderWithStatistics {
	private final String okResponsesPrefix;
	private final String errResponsesPrefix;
	public MathematicalResponsesBuilder(String okResponsesPrefix, String errResponsesPrefix) {
		this.okResponsesPrefix = okResponsesPrefix;
		this.errResponsesPrefix = errResponsesPrefix;
	}

	//========================================

	@Override
	public synchronized String buildOkResponse(String message, long startingMillis) {
		int responseTime = (int) (System.currentTimeMillis() - startingMillis);

		getStatisticsManager().updateStatistics(responseTime);

		return okResponsesPrefix + ';' + NumbersFormatter.millisFormat(responseTime) + ';' + message;
	}

	@Override
	public String buildErrResponse(String message, long startingMillis) {
		return buildErrResponse(message);
	}

	@Override
	@Deprecated
	public String buildOkResponse(String message) {						//considera solo tempo da adesso --> tornerà ragionevolmente zero
		return buildOkResponse(message, System.currentTimeMillis());
	}

	@Override
	public String buildErrResponse(String message) {
		return errResponsesPrefix + ';' + message;
	}
}
