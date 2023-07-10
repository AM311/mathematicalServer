package it.units.in0500908.mathematicalserver.processors;

import it.units.in0500908.lineprocessingserver.RequestsProcessor;
import it.units.in0500908.lineprocessingserver.ResponsesBuilderWithStatistics;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.ComputationRequestsProcessor;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.StatRequestsProcessor;
import it.units.in0500908.utils.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalRequestsProcessor implements RequestsProcessor {
	private final ResponsesBuilderWithStatistics responsesBuilder;
	private final ExecutorService limitedExecutorService;
	private final ExecutorService unlimitedExecutorService;

	public MathematicalRequestsProcessor(ResponsesBuilderWithStatistics responsesBuilder) {
		this.responsesBuilder = responsesBuilder;
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
				futureResponse = unlimitedExecutorService.submit(new StatRequestsProcessor(responsesBuilder.getStatisticsCounter(), request));
			} else if (ComputationRequestsProcessor.isComputationRequest(request)) {
				futureResponse = limitedExecutorService.submit(new ComputationRequestsProcessor(request));
			} else {
				throw new InvalidRequestException("String not matching any kind of accepted request.");
			}

			return responsesBuilder.buildOkResponse(futureResponse.get(), startingMillis);
		} catch (ExecutionException | InterruptedException ex) {
			if (ex.getCause() instanceof Error er) {
				Logger.printLog(System.err, "ERROR while managing client: " + er.getMessage());
			}
			return responsesBuilder.buildErrResponse(ex.getCause().getLocalizedMessage());
		} catch (InvalidRequestException | RuntimeException ex) {
			return responsesBuilder.buildErrResponse(ex.getLocalizedMessage());
		}
	}

	@Override
	public void close() {
		limitedExecutorService.shutdown();
		unlimitedExecutorService.shutdown();
	}
}
