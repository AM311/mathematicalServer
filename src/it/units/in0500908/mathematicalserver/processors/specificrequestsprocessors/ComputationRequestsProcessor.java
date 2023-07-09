package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.SpecificRequestsProcessor;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.ValueTuples;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.VariableValuesFunction;
import it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.expression.Expression;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class ComputationRequestsProcessor implements SpecificRequestsProcessor {
	private final String request;

	public ComputationRequestsProcessor(String request) {
		this.request = request;
	}

	public static boolean isComputationRequest(String request) {
		try {
			return ComputationKindToken.isValid(request.split("_")[0]) && ValuesKindToken.isValid(request.split("_")[1].split(";")[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}

	@Override
	public String call() throws InvalidRequestException {
		String computationKind;
		VariableValuesFunction variableValuesFunction;
		ValueTuples valueTuples;
		Expression[] expressions;

		// >>> ComputationKindToken + OTHERS
		String[] firstSplitTokens = request.split("_");
		computationKind = firstSplitTokens[0];

		if (firstSplitTokens.length != 2) {
			throw new InvalidRequestException("Format not matching any kind of request");
		}

		// >>> ValuesKindToken + variableValuesFunction + Expressions (1+)
		String[] secondSplitTokens = firstSplitTokens[1].split(";");

		if (secondSplitTokens.length < 3) {
			throw new InvalidRequestException("Request not matching Computation Request format");
		}

		// ValueTuples
		try {
			variableValuesFunction = new VariableValuesFunction(secondSplitTokens[1]);
			valueTuples = variableValuesFunction.getTuples(secondSplitTokens[0]);
		} catch (InvalidRequestException ex) {
			throw new InvalidRequestException("Unable to process VariableValuesFunction", ex);
		}

		// Expressions
		expressions = new Expression[secondSplitTokens.length - 2];

		try {
			for (int i = 0; i < secondSplitTokens.length - 2; i++) {
				expressions[i] = new Expression(secondSplitTokens[i + 2]);
			}
		} catch (IllegalArgumentException ex) {
			throw new InvalidRequestException("Invalid computation request: unable to process Expression", ex);
		}

		return getComputationResponse(computationKind, valueTuples, expressions);
	}

	private static String getComputationResponse(String computationKind, ValueTuples tuples, Expression[] expressions) throws InvalidRequestException {
		return switch (ComputationKindToken.parse(computationKind)) {
			case MIN, MAX -> {
				double returnValue = computationKind.equals("MIN") ? Double.MAX_VALUE : Double.MIN_VALUE;

				for (Expression exp : expressions) {
					for (int i = 0; i < tuples.getNumOfTuples(); i++) {
						double res = exp.evaluate(tuples.getTupleByIndex(i));

						if (computationKind.equals("MIN") && Double.compare(res, returnValue) < 0) {
							returnValue = res;
						} else if (computationKind.equals("MAX") && Double.compare(res, returnValue) > 0) {
							returnValue = res;
						}
					}
				}
				yield NumbersFormatter.decimalFormat(returnValue);
			}
			case AVG -> {
				double avgValue = 0;

				for (int i = 0; i < tuples.getNumOfTuples(); i++) {
					avgValue += expressions[0].evaluate(tuples.getTupleByIndex(i));
				}

				yield NumbersFormatter.decimalFormat(avgValue / tuples.getNumOfTuples());
			}
			case COUNT -> NumbersFormatter.decimalFormat(tuples.getNumOfTuples());
		};
	}

	public enum ComputationKindToken {
		MIN("MIN"), MAX("MAX"), AVG("AVG"), COUNT("COUNT");

		private final String tokenString;

		ComputationKindToken(String tokenString) {
			this.tokenString = tokenString;
		}

		public static boolean isValid(String computationKind) {
			try {
				parse(computationKind);
			} catch (InvalidRequestException e) {
				return false;
			}

			return true;
		}

		public static ComputationKindToken parse(String computationKind) throws InvalidRequestException {
			for (ComputationKindToken token : ComputationKindToken.values()) {
				if (token.tokenString.equals(computationKind)) {
					return token;
				}
			}

			throw new InvalidRequestException("Invalid ComputationKindToken string!");
		}

		public String getTokenString() {
			return this.tokenString;
		}
	}

	public enum ValuesKindToken {
		GRID("GRID"), LIST("LIST");

		private final String tokenString;

		ValuesKindToken(String tokenString) {
			this.tokenString = tokenString;
		}

		public static boolean isValid(String valuesKind) {
			try {
				parse(valuesKind);
			} catch (InvalidRequestException e) {
				return false;
			}

			return true;
		}

		public static ValuesKindToken parse(String valuesKind) throws InvalidRequestException {
			for (ValuesKindToken token : ValuesKindToken.values()) {
				if (token.tokenString.equals(valuesKind)) {
					return token;
				}
			}

			throw new InvalidRequestException("Invalid ValuesKindToken string!");
		}

		public String getTokenString() {
			return this.tokenString;
		}
	}
}
