package it.units.in0500908.mathematicalserver.handlers;

import it.units.in0500908.lineprocessingserver.RequestsProcessor;
import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.lineprocessingserver.SpecificRequestProcessor;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.handlers.specificrequestshandlers.ComputationRequestsProcessor;
import it.units.in0500908.mathematicalserver.handlers.specificrequestshandlers.StatRequestsProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalRequestsHandler implements RequestsProcessor {
	private final ResponsesProcessorWithStatistics responsesProcessor;            //Si basa su Responses Processor (eventualità qui decisa)
	//ResponsesHandler definito internamente perché dipende STRETTAMENTE dal corpo di questa classe!

	public MathematicalRequestsHandler() {
		responsesProcessor = new MathematicalResponsesProcessor("OK", "ERR");
	}

	//-----------------
	@Override
	public String process(String request) {
		long startingMillis = System.currentTimeMillis();
		SpecificRequestProcessor specificRequestProcessor;

		try {
			if (isStatRequest(request)) {
				synchronized (responsesProcessor) {													//todo spostare, così non funziona correttamente
					specificRequestProcessor = new StatRequestsProcessor(responsesProcessor);
					return responsesProcessor.buildOkResponse(specificRequestProcessor.process(request), startingMillis);
				}
			} else if (isComputationRequest(request)) {
				specificRequestProcessor = new ComputationRequestsProcessor();
			} else {
				throw new InvalidRequestException("String not matching any kind of accepted request.");
			}

			return responsesProcessor.buildOkResponse(specificRequestProcessor.process(request), startingMillis);
		} catch (InvalidRequestException ex) {
			return responsesProcessor.buildErrResponse(ex.getLocalizedMessage());
		}
	}

	//---------------

	private boolean isStatRequest(String req) {
		List<String> statRequests = Arrays.asList("STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME");

		return statRequests.contains(req);
	}

	private boolean isComputationRequest(String req) {
		String regex = "^(AVG|MAX|MIN|COUNT)(_)(GRID|LIST)(;)(.*)";        //verifica solo primi due parametri per accettare lo smistamento

		return req.matches(regex);
	}
}
