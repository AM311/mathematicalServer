package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.ResponsesBuilderWithStatistics;
import it.units.in0500908.lineprocessingserver.SpecificRequestsProcessor;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.utils.NumbersFormatter;

import java.util.Set;

/**
 * @author Alessio Manià - IN0500908
 */
public class StatRequestsProcessor implements SpecificRequestsProcessor {
	private final ResponsesBuilderWithStatistics responsesBuilder;        //Necessario per accedere alle statistiche
	private final String request;                                         //Necessario: callable non accetta argomenti

	public StatRequestsProcessor(ResponsesBuilderWithStatistics responsesBuilder, String request) {
		this.responsesBuilder = responsesBuilder;
		this.request = request;
	}

	@Override
	public String call() throws InvalidRequestException {				//todo elaborare direttamente risposte qui
		switch (request) {
			case "STAT_REQS" -> {
				return String.valueOf(responsesBuilder.getResponsesCounter());
			}
			case "STAT_AVG_TIME" -> {
				return NumbersFormatter.millisFormat(responsesBuilder.getAvgResponseTime());
			}
			case "STAT_MAX_TIME" -> {
				return NumbersFormatter.millisFormat(responsesBuilder.getMaxResponseTime());
			}
		}

		throw new InvalidRequestException("Invalid Stat Request!");
	}

	public static boolean isStatRequest(String request) {
		final Set<String> statRequests = Set.of("STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME");

		return statRequests.contains(request);
	}
}
