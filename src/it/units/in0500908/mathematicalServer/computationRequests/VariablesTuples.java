package it.units.in0500908.mathematicalServer.computationRequests;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Alessio Manià - IN0500908
 */
public class VariablesTuples {
	private LinkedHashMap<String, Integer> variables;                        //Codifica nomi e posizioni relative
	private double[][] values;
	public VariablesTuples(String[] varNames, double[][] values) throws IllegalArgumentException {			//todo custom exception?
		if (!isValid(varNames, values))
			throw new IllegalArgumentException("Impossible to create a Tuple: invalid arguments!");

		this.values = values;

		variables = new LinkedHashMap<>();
		for (int i = 0; i < varNames.length; i++) {
			variables.put(varNames[i], i);
		}
	}

	private boolean isValid(String[] varNames, double[][] values) {
		int length = -1;

		for (double[] entry : values) {
			if (length == -1) {
				length = entry.length;
			} else {
				if (entry.length != length)
					return false;
			}
		}

		return varNames.length == length;
	}
	//---------
	public HashMap<String, Double> getValuesByIndex(int index) throws IndexOutOfBoundsException {
		if (index > values.length) {
			throw new IndexOutOfBoundsException("Tuple has less then " + index + " elements!");
		}

		HashMap<String, Double> iValues = new HashMap<>();
		String[] varNames = new String[variables.size()];
		variables.keySet().toArray(varNames);

		for (int i = 0; i < varNames.length; i++) {
			iValues.put(varNames[i], values[index][variables.get(varNames[i])]);
		}

		return iValues;
	}

	public int getTupleLength() {
		return values.length;
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

		for (double[] valTuple : values) {
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
