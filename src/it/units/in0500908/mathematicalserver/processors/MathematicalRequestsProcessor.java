package it.units.in0500908.mathematicalserver.processors;

import it.units.in0500908.lineprocessingserver.RequestsProcessor;
import it.units.in0500908.lineprocessingserver.ResponsesBuilderWithStatistics;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.ComputationRequestsProcessor;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.StatRequestsProcessor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalRequestsProcessor implements RequestsProcessor {
	private final ResponsesBuilderWithStatistics responsesProcessor;    //Si basa su Responses Processor (eventualità qui decisa)
	//ResponsesHandler definito internamente: dipende STRETTAMENTE da questa classe!
	private final ExecutorService limitedExecutorService;
	private final ExecutorService unlimitedExecutorService;

	public MathematicalRequestsProcessor() {
		responsesProcessor = new MathematicalResponsesBuilder("OK", "ERR");
		unlimitedExecutorService = Executors.newCachedThreadPool();
		limitedExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	//-----------------
	@Override
	public String process(String request) {
		long startingMillis = System.currentTimeMillis();
		Future<String> futureResponse;

		try {
			if (StatRequestsProcessor.isStatRequest(request)) {
				futureResponse = unlimitedExecutorService.submit(new StatRequestsProcessor(responsesProcessor, request));
			} else if (ComputationRequestsProcessor.isComputationRequest(request)) {
				futureResponse = limitedExecutorService.submit(new ComputationRequestsProcessor(request));
			} else {
				throw new InvalidRequestException("String not matching any kind of accepted request.");
			}

			return responsesProcessor.buildOkResponse(futureResponse.get(), startingMillis);
		} catch (ExecutionException | InterruptedException ex) {
			return responsesProcessor.buildErrResponse(ex.getCause().getLocalizedMessage());
		} catch (InvalidRequestException | RuntimeException ex) {
			return responsesProcessor.buildErrResponse(ex.getLocalizedMessage());
		}
	}
}
