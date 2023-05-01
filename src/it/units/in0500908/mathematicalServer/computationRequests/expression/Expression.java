package it.units.in0500908.mathematicalServer.computationRequests.expression;

import java.util.HashMap;

/**
 * @author Alessio Manià - IN0500908
 */
public class Expression {
	private Node element;                        //todo basta solo questo?!

	public Expression(String expression) {
		Parser parser = new Parser(expression);
		element = parser.parse();
	}

	public double evaluate(HashMap<String, Double> iTuple) {
		return element.evaluate(iTuple);
	}
}
