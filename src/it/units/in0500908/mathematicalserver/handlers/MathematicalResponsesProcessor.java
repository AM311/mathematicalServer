package it.units.in0500908.mathematicalserver.handlers;

import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalResponsesProcessor extends ResponsesProcessorWithStatistics {
	public MathematicalResponsesProcessor(String okResponsesMessage, String errResponsesMessage) {
		super(okResponsesMessage, errResponsesMessage);
	}

	//========================================

	//todo aggiungere campi statici per gestione STAT

	@Override
	public synchronized String buildOkResponse(String message, long startingMillis) {
		int responseTime = (int) (System.currentTimeMillis() - startingMillis);

		updateCounters(responseTime);

		return "OK" + ';' + NumbersFormatter.millisFormat(responseTime) + ';' + message;
	}

	@Override
	public String buildErrResponse(String message) {
		return "ERR" + ';' + message;
	}
}
