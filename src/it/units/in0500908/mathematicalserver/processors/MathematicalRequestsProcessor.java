package it.units.in0500908.mathematicalserver.processors;

import it.units.in0500908.lineprocessingserver.RequestsProcessor;
import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.ComputationRequestsProcessor;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.StatRequestsProcessor;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalRequestsProcessor implements RequestsProcessor {
	private final ResponsesProcessorWithStatistics responsesProcessor;            //Si basa su Responses Processor (eventualità qui decisa)
	//ResponsesHandler definito internamente perché dipende STRETTAMENTE dal corpo di questa classe!
	private final ExecutorService limitedExecutorService;
	private final ExecutorService executorService;

	public MathematicalRequestsProcessor() {
		responsesProcessor = new MathematicalResponsesProcessor("OK", "ERR");
		executorService = Executors.newCachedThreadPool();
		limitedExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	//-----------------
	@Override
	public String process(String request) {
		long startingMillis = System.currentTimeMillis();
		Future<String> futureResponse;

		try {
			if (isStatRequest(request)) {
				synchronized (responsesProcessor) {                                                    //todo spostare, così non funziona correttamente
					futureResponse = executorService.submit(new StatRequestsProcessor(responsesProcessor, request));
				}
			} else if (isComputationRequest(request)) {
				futureResponse = limitedExecutorService.submit(new ComputationRequestsProcessor(request));
			} else {
				throw new InvalidRequestException("String not matching any kind of accepted request.");
			}

			String res = futureResponse.get();
			return responsesProcessor.buildOkResponse(res, startingMillis);
		} catch (ExecutionException | InterruptedException ex) {
			return responsesProcessor.buildErrResponse(ex.getCause().getLocalizedMessage());
		} catch (InvalidRequestException ex) {
			return responsesProcessor.buildErrResponse(ex.getLocalizedMessage());
		}
	}

	//---------------
	//todo valutare se spostare in struttura globale elenco parole chiave richieste

	private boolean isStatRequest(String req) {
		final Set<String> statRequests = Set.of("STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME");

		return statRequests.contains(req);
	}

	private boolean isComputationRequest(String req) {
		final String regex = "^(AVG|MAX|MIN|COUNT)(_)(GRID|LIST)(;)(.*)";        //verifica solo primi due parametri per accettare lo smistamento

		return req.matches(regex);
	}
}
