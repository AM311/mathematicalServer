package it.units.in0500908.mathematicalServer;

import it.units.in0500908.mathematicalServer.computationRequests.VariableValuesFunction;
import it.units.in0500908.mathematicalServer.computationRequests.VariablesTuples;
import it.units.in0500908.mathematicalServer.computationRequests.expression.Expression;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alessio Manià - IN0500908
 */
public class RequestsHandler {

	//Not-instantiable class.
	private RequestsHandler() {
	}

	//-----------------

	//todo definire due handler specifici per i tipi di richieste: derivano da classe astratta SpecificRequestHandler con due metodi (isreq, handlereq)
	public static String processRequest(String req) {
		try {
			if (isStatRequest(req)) {
				return ResponsesHandler.getStatResponse(req, System.currentTimeMillis());
			} else {
				return requestDecoder(req);
			}
		} catch (InvalidRequestException ex) {
			return ResponsesHandler.buildErrResponse(ex.getLocalizedMessage());
		}
	}

	private static boolean isStatRequest(String req) {
		List<String> statRequests = Arrays.asList("STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME");

		return statRequests.contains(req);
	}

	private static String requestDecoder(String req) throws InvalidRequestException {
		List<String> computationKinds = Arrays.asList("MIN", "MAX", "AVG", "COUNT");
		List<String> valuesKinds = Arrays.asList("GRID", "LIST");

		String computationKind;
		VariableValuesFunction vvf;
		VariablesTuples variablesTuples;
		Expression[] expressions;

		// >>> ComputationKind + OTHERS
		String[] firstSplit = req.split("_");
		computationKind = firstSplit[0];

		if (firstSplit.length != 2 || !computationKinds.contains(firstSplit[0])) {
			throw new InvalidRequestException("Format not matching any kind of request");
		}

		// >>> ValuesKind + variableValuesFunction + Expressions (1+)
		String[] secondSplit = firstSplit[1].split(";");

		if (secondSplit.length < 3 || !valuesKinds.contains(secondSplit[0])) {
			throw new InvalidRequestException("Format not matching any kind of request");
		}

		// VariablesTuples
		vvf = new VariableValuesFunction(secondSplit[1]);
		variablesTuples = vvf.getTuples(secondSplit[0]);

		// Expressions
		expressions = new Expression[secondSplit.length - 2];

		try {
			for (int i = 0; i < secondSplit.length - 2; i++) {
				expressions[i] = new Expression(secondSplit[i + 2]);
			}
		} catch (IllegalArgumentException ex) {
			throw new InvalidRequestException("Invalid computation request: unable to process Expression", ex);
		}

		return ResponsesHandler.getComputationResponse(computationKind, variablesTuples, expressions, System.currentTimeMillis());
	}
}
