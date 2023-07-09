package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests.expression;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Operator extends Node {

	public enum Type {
		SUM('+', a -> a[0] + a[1]),
		SUBTRACTION('-', a -> a[0] - a[1]),
		MULTIPLICATION('*', a -> a[0] * a[1]),
		DIVISION('/', a -> a[0] / a[1]),
		POWER('^', a -> Math.pow(a[0], a[1]));
		private final char symbol;
		private final Function<double[], Double> function;

		Type(char symbol, Function<double[], Double> function) {
			this.symbol = symbol;
			this.function = function;
		}

		public char getSymbol() {
			return symbol;
		}

		public Function<double[], Double> getFunction() {
			return function;
		}
	}

	private final Type type;

	public Operator(Type type, List<Node> children) {
		super(children);
		this.type = type;
	}

	@Override
	public double evaluate(Map<String, Double> iTuple) throws InvalidRequestException {
		Node[] children = new Node[2];
		this.getChildren().toArray(children);

		return type.function.apply(new double[]{children[0].evaluate(iTuple), children[1].evaluate(iTuple)});
	}

	public Type getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Operator operator = (Operator) o;
		return type == operator.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public String toString() {
		return "(" +
				getChildren().stream()
						.map(Node::toString)
						.collect(Collectors.joining(String.valueOf(type.symbol))) +
				")";
	}
}
