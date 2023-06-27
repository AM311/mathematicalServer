package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.lineprocessingserver.SpecificRequestHandler;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class StatRequestsProcessor implements SpecificRequestHandler {
	private final ResponsesProcessorWithStatistics responsesProcessor;
	private final String request;

	public StatRequestsProcessor(ResponsesProcessorWithStatistics responsesProcessor, String request) {
		this.responsesProcessor = responsesProcessor;
		this.request = request;
	}

	@Override
	public String call() throws InvalidRequestException {
		switch (request) {
			case "STAT_REQS" -> {
				return String.valueOf(responsesProcessor.getOkResponsesCounter());
			}
			case "STAT_AVG_TIME" -> {
				return NumbersFormatter.millisFormat(responsesProcessor.getAvgResponseTime());
			}
			case "STAT_MAX_TIME" -> {
				return NumbersFormatter.millisFormat(responsesProcessor.getMaxResponseTime());
			}
		}

		throw new InvalidRequestException("Invalid Stat Request!");
	}

}
