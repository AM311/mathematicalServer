package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.SpecificRequestHandler;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.VariableValuesFunction;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.VariablesTuples;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.expression.Expression;
import it.units.in0500908.utils.NumbersFormatter;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Alessio Manià - IN0500908
 */
public class ComputationRequestsProcessor implements SpecificRequestHandler {
	private final String request;

	public ComputationRequestsProcessor(String request) {
		this.request = request;
	}

	@Override
	public String call() throws InvalidRequestException {
		Set<String> computationKinds = Set.of("MIN", "MAX", "AVG", "COUNT");            //todo valutare se togliere (già verificato a monte)
		Set<String> valuesKinds = Set.of("GRID", "LIST");

		String computationKind;
		VariableValuesFunction vvf;                                                    //todo valutare se conviene modellare solo una funzione nella classe
		VariablesTuples variablesTuples;
		Expression[] expressions;

		// >>> ComputationKind + OTHERS
		String[] firstSplit = request.split("_");
		computationKind = firstSplit[0];

		if (firstSplit.length != 2 || !computationKinds.contains(computationKind)) {
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

		return getComputationResponse(computationKind, variablesTuples, expressions);
	}

	public static String getComputationResponse(String computationKind, VariablesTuples tuples, Expression[] expressions) throws InvalidRequestException {
		switch (computationKind) {
			case "MIN", "MAX" -> {
				double returnValue = computationKind.equals("MIN") ? Double.MAX_VALUE : Double.MIN_VALUE;

				for (Expression exp : expressions) {
					for (int i = 0; i < tuples.getTupleLength(); i++) {
						double res = exp.evaluate(tuples.getValuesByIndex(i));

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

				for (int i = 0; i < tuples.getTupleLength(); i++) {
					avgValue += expressions[0].evaluate(tuples.getValuesByIndex(i));
				}

				return NumbersFormatter.decimalFormat(avgValue / tuples.getTupleLength());
			}
			case "COUNT" -> {
				return String.valueOf(tuples.getTupleLength());
			}
		}

		throw new InvalidRequestException("Unhandled Computation Request");
	}
}