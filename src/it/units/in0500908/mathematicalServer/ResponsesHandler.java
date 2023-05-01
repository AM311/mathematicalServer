package it.units.in0500908.mathematicalServer;

import it.units.in0500908.mathematicalServer.computationRequests.VariablesTuples;
import it.units.in0500908.mathematicalServer.computationRequests.expression.Expression;

import java.util.HashMap;
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

	public static String getStatResponse(String req, long startingTime) {
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

		return buildErrResponse("Unhandled Stat Request");
	}

	public static String getComputationResponse(String computationKind, VariablesTuples tuples, Expression[] expressions, long startingTime) {
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
			return buildErrResponse("Unable to compute a response: " + ex.getMessage());
		}

		return buildErrResponse("Unhandled Computation Request.");
	}

	//----------------------------
	private static String buildOkResponse(String response, long startingTime) {
		int responseTime = (int) (System.currentTimeMillis() - startingTime);

		ResponsesHandler.updateCounters(responseTime);

		return "OK" + ';' + millisFormat(responseTime) + ';' + response;
	}

	private static String millisFormat(long millis) {
		double seconds = (double) millis / 1000.0;
		return String.format(Locale.US, "%.3f", seconds);
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
