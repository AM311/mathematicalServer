package it.units.in0500908.mathematicalserver.handlers.specificrequestsprocessors.computationrequests.expression;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Variable extends Node {
	private final String name;

	public Variable(String name) {
		super(Collections.emptyList());
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public double evaluate(Map<String, Double> iTuple) throws InvalidRequestException {
		try {
			return iTuple.get(name);
		} catch (NullPointerException ex) {
			throw new InvalidRequestException("Trying to call a non-declared variable");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Variable variable = (Variable) o;
		return Objects.equals(name, variable.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name);
	}

	@Override
	public String toString() {
		return name;
	}
}