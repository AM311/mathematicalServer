package it.units.in0500908.mathematicalserver.handlers;

import it.units.in0500908.lineprocessingserver.RequestsProcessor;
import it.units.in0500908.lineprocessingserver.ResponsesProcessorWithStatistics;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.handlers.specificrequestsprocessors.ComputationRequestsProcessor;
import it.units.in0500908.mathematicalserver.handlers.specificrequestsprocessors.StatRequestsProcessor;

import java.util.Set;
import java.util.concurrent.*;

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
		limitedExecutorService = Executors.newCachedThreadPool();
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	//-----------------
	@Override
	public String process(String request) {
		long startingMillis = System.currentTimeMillis();
		Future<String> futureResponse;

		try (executorService) {
			if (isStatRequest(request)) {
				synchronized (responsesProcessor) {                                                    //todo spostare, così non funziona correttamente
					//return responsesProcessor.buildOkResponse(specificRequestProcessor.process(request), startingMillis);
					futureResponse = executorService.submit(new StatRequestsProcessor(responsesProcessor, request));
				}
			} else if (isComputationRequest(request)) {
				//specificRequestProcessor = new ComputationRequestsProcessor();
				futureResponse = limitedExecutorService.submit(new ComputationRequestsProcessor(request));
			} else {
				throw new InvalidRequestException("String not matching any kind of accepted request.");
			}

			return responsesProcessor.buildOkResponse(futureResponse.get(), startingMillis);
		} catch (InvalidRequestException ex) {
			return responsesProcessor.buildErrResponse(ex.getLocalizedMessage());
		} catch (ExecutionException | InterruptedException ex) {
			return responsesProcessor.buildErrResponse("Error during computation" + ex.getLocalizedMessage());
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
