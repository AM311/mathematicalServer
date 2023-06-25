package it.units.in0500908.mathematicalserver.handlers.specificrequestsprocessors.computationrequests.expression;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alessio Manià - IN0500908
 */
public class Expression {
	private Node element;                        //todo basta solo questo?!

	public Expression(String expression) {
		Parser parser = new Parser(expression);
		element = parser.parse();
	}

	public double evaluate(Map<String, Double> iTuple) throws InvalidRequestException {
		return element.evaluate(iTuple);
	}
}
