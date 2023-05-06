package it.units.in0500908.mathematicalServer;

import it.units.in0500908.mathematicalServer.computationRequests.VariablesTuples;
import it.units.in0500908.mathematicalServer.computationRequests.expression.Expression;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Alessio Manià - IN0500908
 */
public class ResponsesHandler {
	private static int okResponsesCounter = 0;
	private static int sumOfResponseTime = 0;
	private static int maxResponseTime = 0;

	//Not-instantiable class.
	private ResponsesHandler() {
	}

	//----------------------------

	//TODO gestire correttamente (synchronized?)

	public static String getStatResponse(String req, long startingTime) throws InvalidRequestException {
		switch (req) {
			case "STAT_REQS" -> {
				return buildOkResponse(String.valueOf(okResponsesCounter), startingTime);
			}
			case "STAT_AVG_TIME" -> {
				return buildOkResponse(millisFormat(sumOfResponseTime / okResponsesCounter), startingTime);
			}
			case "STAT_MAX_TIME" -> {
				return buildOkResponse(millisFormat(maxResponseTime), startingTime);
			}
		}

		throw new InvalidRequestException();
	}

	public static String getComputationResponse(String computationKind, VariablesTuples tuples, Expression[] expressions, long startingTime) throws InvalidRequestException {
		try {
			switch (computationKind) {            //todo provare a vedere se si riesce a gestire sia qui che RequestsHandler come Operator con ENUM e FUNCTION
				case "MIN", "MAX" -> {
					double returnValue = computationKind.equals("MIN") ? Double.MAX_VALUE : Double.MIN_VALUE;

					for (Expression exp : expressions) {
						for (int i = 0; i < tuples.getTupleLength(); i++) {
							double res = exp.evaluate(tuples.getValuesByIndex(i));

							if (computationKind.equals("MIN") && res < returnValue) {
								returnValue = res;
							} else if (computationKind.equals("MAX") && res > returnValue) {
								returnValue = res;
							}
						}
					}

					return buildOkResponse(String.valueOf(returnValue), startingTime);
				}
				case "AVG" -> {
					double avgValue = 0;

					for (int i = 0; i < tuples.getTupleLength(); i++) {
						avgValue += expressions[0].evaluate(tuples.getValuesByIndex(i));
					}

					return buildOkResponse(String.valueOf(avgValue / tuples.getTupleLength()), startingTime);
				}
				case "COUNT" -> {
					return buildOkResponse(String.valueOf(tuples.getTupleLength()), startingTime);
				}
			}
		} catch (RuntimeException ex) {
			throw new InvalidRequestException("Unable to compute a response", ex);
		}

		throw new InvalidRequestException("Unhandled Computation Request");
	}

	//----------------------------
	private static String buildOkResponse(String response, long startingTime) {
		int responseTime = (int) (System.currentTimeMillis() - startingTime);

		ResponsesHandler.updateCounters(responseTime);

		return "OK" + ';' + millisFormat(responseTime) + ';' + response;
	}

	private static String millisFormat(long millis) {					//todo spostare su classe a parte assieme a formatter per double
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.#", dfs);
		df.setMinimumFractionDigits(3);
		df.setGroupingUsed(false);

		double seconds = (double) millis / 1000.0;
		return df.format(seconds);
	}

	private static void updateCounters(int responseTime) {
		ResponsesHandler.okResponsesCounter++;
		ResponsesHandler.sumOfResponseTime += responseTime;
		ResponsesHandler.maxResponseTime = Math.max(ResponsesHandler.maxResponseTime, responseTime);
	}

	//---
	public static String buildErrResponse(String errorMessage) {
		return "ERR" + ';' + errorMessage;
	}
}
