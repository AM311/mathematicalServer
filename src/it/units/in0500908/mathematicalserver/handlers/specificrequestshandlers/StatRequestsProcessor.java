package it.units.in0500908.mathematicalserver.handlers.specificrequestshandlers;

import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.lineprocessingserver.SpecificRequestProcessor;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class StatRequestsProcessor implements SpecificRequestProcessor {
	private final ResponsesProcessorWithStatistics responsesProcessor;

	public StatRequestsProcessor(ResponsesProcessorWithStatistics responsesProcessor) {
		this.responsesProcessor = responsesProcessor;
	}

	@Override
	public String process(String request) throws InvalidRequestException {
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
