package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.expression;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

import java.util.Map;

/**
 * @author Alessio Manià - IN0500908
 */
public class Expression {
	private final Node element;

	public Expression(String expression) throws InvalidRequestException {
		Parser parser = new Parser(expression);
		element = parser.parse();

		if (!element.toString().replace(" ", "").equals(expression)) {
			throw new InvalidRequestException(String.format("Expression %s is not matching the requested format.", expression));
		}
	}

	public double evaluate(Map<String, Double> iTuple) throws InvalidRequestException {
		return element.evaluate(iTuple);
	}
}
