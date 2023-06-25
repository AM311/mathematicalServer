package it.units.in0500908.mathematicalserver.handlers.specificrequestsprocessors.computationrequests;

import java.util.*;

/**
 * @author Alessio Manià - IN0500908
 */
public class VariablesTuples {
	private final Map<String, Integer> variables;                        //Codifica nomi e posizioni relative
	//private final double[][] values;
	private final List<List<Double>> values;							//List<List> e non Set<List> per coerenza con la notazione della consegna
	//public VariablesTuples(String[] varNames, double[][] values) throws IllegalArgumentException {
	public VariablesTuples(List<String> varNames, List<List<Double>> values) throws IllegalArgumentException {			//todo custom exception?
		if (!isValid(varNames, values))
			throw new IllegalArgumentException("Impossible to create a Tuple: invalid arguments!");

		this.values = values;

		variables = new LinkedHashMap<>();					//mantiene ordine
		for (int i = 0; i < varNames.size(); i++) {
			variables.put(varNames.get(i), i);
		}
	}

	private boolean isValid(List<String> varNames, List<List<Double>> values) {
		int size = -1;

		for (List<Double> entry : values) {
			if (size == -1) {
				size = entry.size();
			} else {
				if (entry.size() != size)
					return false;
			}
		}

		return varNames.size() == size;
	}

	//---------

	public Map<String, Double> getValuesByIndex(int index) throws IndexOutOfBoundsException {
		if (index > values.size()) {
			throw new IndexOutOfBoundsException("Tuple has less then " + index + " elements!");
		}

		Map<String, Double> iValues = new HashMap<>();
		//String[] varNames = new String[variables.size()];
		//variables.keySet().toArray(varNames);

		for (String varName : variables.keySet()) {											//Ordine è garantito: LinkedHashMap --> SortedSet in runtime
			//iValues.put(varName, values[index][variables.get(varName)]);
			iValues.put(varName, values.get(index).get(variables.get(varName)));
		}

		return iValues;
	}

	public int getTupleLength() {
		return values.size();
	}

	//---------

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append("T(");
		for (String key : variables.keySet()) {
			out.append(key).append(",");
		}
		out.deleteCharAt(out.length() - 1);
		out.append(")=(");

		for (List<Double> valTuple : values) {
			out.append('(');

			for (double val : valTuple) {
				out.append(val).append(",");
			}

			out.deleteCharAt(out.length() - 1);
			out.append("),");
		}

		out.deleteCharAt(out.length() - 1);
		out.append(")").append(System.lineSeparator());

		return out.toString();
	}
}
