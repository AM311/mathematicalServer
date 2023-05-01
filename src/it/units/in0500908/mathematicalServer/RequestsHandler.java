package it.units.in0500908.mathematicalServer;

import it.units.in0500908.mathematicalServer.computationRequests.VariablesTuples;
import it.units.in0500908.mathematicalServer.computationRequests.VariableValuesFunction;
import it.units.in0500908.mathematicalServer.computationRequests.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
		if (isStatRequest(req)) {
			return ResponsesHandler.getStatResponse(req, System.currentTimeMillis());
		} else {
			return computationRequestDecoder(req);
			//return it.units.in0500908.mathematicalServer.ResponsesHandler.getComputationResponse(req, System.currentTimeMillis());		//todo mettere dopo nel Decoder
		}
	}

	private static boolean isStatRequest(String req) {
		String[] statRequests = {"STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME"};
		//todo passare ad arraylist
		return Arrays.asList(statRequests).contains(req);
	}

	private static String computationRequestDecoder(String req) {
		//todo gestire casi degeneri con uno o più parametri vuoti

		ArrayList<String> computationKinds = new ArrayList<>(Arrays.asList("MIN", "MAX", "AVG", "COUNT"));
		ArrayList<String> valuesKinds = new ArrayList<>(Arrays.asList("GRID", "LIST"));

		String computationKind;
		VariableValuesFunction vvf;
		VariablesTuples variablesTuples;
		Expression[] expressions;

		// >>> ComputationKind + OTHERS
		String[] firstSplit = req.split("_");
		computationKind = firstSplit[0];

		if (firstSplit.length != 2 || !computationKinds.contains(firstSplit[0])) {
			return ResponsesHandler.buildErrResponse("Invalid request: format not matching any kind of request.");
		}

		// >>> ValuesKind + variableValuesFunction + Expressions (1+)
		String[] secondSplit = firstSplit[1].split(";");

		if (secondSplit.length < 3 || !valuesKinds.contains(secondSplit[0])) {
			return ResponsesHandler.buildErrResponse("Invalid request: format not matching any kind of request.");
		}

		// VariablesTuples
		try {
			vvf = new VariableValuesFunction(secondSplit[1]);
			variablesTuples = vvf.getTuples(secondSplit[0]);
		} catch (RuntimeException ex) {
			return ResponsesHandler.buildErrResponse("Invalid computation request: " + ex.getMessage());
		}

		// Expressions
		expressions = new Expression[secondSplit.length - 2];

		try {
			for (int i = 0; i < secondSplit.length - 2; i++) {
				expressions[i] = new Expression(secondSplit[i + 2]);
			}
		} catch (RuntimeException ex) {
			return ResponsesHandler.buildErrResponse("Invalid computation request: " + ex.getMessage());
		}

		return ResponsesHandler.getComputationResponse(computationKind, variablesTuples, expressions, System.currentTimeMillis());
	}
}
