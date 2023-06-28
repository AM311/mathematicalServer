package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.SpecificRequestsProcessor;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.ValueTuples;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.VariableValuesFunction;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.expression.Expression;
import it.units.in0500908.utils.Logger;
import it.units.in0500908.utils.NumbersFormatter;

import java.util.Set;

/**
 * @author Alessio Manià - IN0500908
 */
public class ComputationRequestsProcessor implements SpecificRequestsProcessor {
	private final String request;                            //Necessario: callable non accetta parametri

	public ComputationRequestsProcessor(String request) {
		this.request = request;
	}

	public static boolean isComputationRequest(String request) {
		final String regex = "^(AVG|MAX|MIN|COUNT)(_)(GRID|LIST)(;)(.*)";

		return request.matches(regex);
	}

	@Override
	public String call() throws InvalidRequestException {
		Set<String> computationKinds = Set.of("MIN", "MAX", "AVG", "COUNT");            //todo valutare se togliere (già verificato a monte)
		Set<String> valuesKinds = Set.of("GRID", "LIST");

		String computationKind;
		VariableValuesFunction variableValuesFunction;
		ValueTuples valueTuples;
		Expression[] expressions;

		// >>> ComputationKind + OTHERS
		String[] firstSplitTokens = request.split("_");
		computationKind = firstSplitTokens[0];

		if (firstSplitTokens.length != 2 || !computationKinds.contains(computationKind)) {			//todo togliere contains qui e sotto
			throw new InvalidRequestException("Format not matching any kind of request");
		}

		// >>> ValuesKind + variableValuesFunction + Expressions (1+)
		String[] secondSplitTokens = firstSplitTokens[1].split(";");

		if (secondSplitTokens.length < 3 || !valuesKinds.contains(secondSplitTokens[0])) {
			throw new InvalidRequestException("Format not matching any kind of request");
		}

		// ValueTuples
		variableValuesFunction = new VariableValuesFunction(secondSplitTokens[1]);
		valueTuples = variableValuesFunction.getTuples(secondSplitTokens[0]);

		// Expressions
		expressions = new Expression[secondSplitTokens.length - 2];

		try {
			for (int i = 0; i < secondSplitTokens.length - 2; i++) {
				expressions[i] = new Expression(secondSplitTokens[i + 2]);
			}
		} catch (IllegalArgumentException ex) {
			throw new InvalidRequestException("Invalid computation request: unable to process Expression", ex);
		}

		Logger.printLog(System.out, valueTuples.toString());            //TEST

		return getComputationResponse(computationKind, valueTuples, expressions);
	}

	private static String getComputationResponse(String computationKind, ValueTuples tuples, Expression[] expressions) throws InvalidRequestException {
		switch (computationKind) {
			case "MIN", "MAX" -> {
				double returnValue = computationKind.equals("MIN") ? Double.MAX_VALUE : Double.MIN_VALUE;

				for (Expression exp : expressions) {
					for (int i = 0; i < tuples.getNumOfTuples(); i++) {
						double res = exp.evaluate(tuples.getTupleByIndex(i));

						if (computationKind.equals("MIN") && Double.compare(res, returnValue) < 0) {                //Comparazione tramite Double gestisce NaN
							returnValue = res;
						} else if (computationKind.equals("MAX") && Double.compare(res, returnValue) > 0) {
							returnValue = res;
						}
					}
				}
				return NumbersFormatter.decimalFormat(returnValue);
			}
			case "AVG" -> {
				double avgValue = 0;

				for (int i = 0; i < tuples.getNumOfTuples(); i++) {
					avgValue += expressions[0].evaluate(tuples.getTupleByIndex(i));
				}

				return NumbersFormatter.decimalFormat(avgValue / tuples.getNumOfTuples());
			}
			case "COUNT" -> {
				return String.valueOf(tuples.getNumOfTuples());
			}
		}

		throw new InvalidRequestException("Unhandled Computation Request");
	}
}
