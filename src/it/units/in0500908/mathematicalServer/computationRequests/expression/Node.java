package it.units.in0500908.mathematicalServer.computationRequests.expression;

import it.units.in0500908.mathematicalServer.InvalidRequestException;

import java.io.InvalidClassException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class Node {
	private final List<Node> children;

	public Node(List<Node> children) {
		this.children = children;
	}

	public List<Node> getChildren() {
		return children;
	}

	public abstract double evaluate(HashMap<String, Double> iTuple) throws InvalidRequestException;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node node = (Node) o;
		return Objects.equals(children, node.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(children);
	}

}
