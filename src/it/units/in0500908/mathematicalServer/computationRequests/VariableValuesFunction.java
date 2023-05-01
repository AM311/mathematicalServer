package it.units.in0500908.mathematicalServer.computationRequests;

import java.util.LinkedHashMap;

/**
 * @author Alessio Manià - IN0500908
 */
public class VariableValuesFunction {
	private final LinkedHashMap<String, double[]> variablesValuesTuples;                    //NOTA! MANTIENE ORDINE!

	public VariableValuesFunction(String variableValuesFunction) {
		this.variablesValuesTuples = new LinkedHashMap<>();
		parseFunction(variableValuesFunction);
	}

	//----------------

	private void parseFunction(String function) throws RuntimeException {
		String[] variablesValues = function.split(",");

		for (int i = 0; i < variablesValues.length; i++) {
			String[] values = variablesValues[i].split(":");

			try {
				String varName;
				if (values[0].matches("[a-z][a-z0-9]*"))
					varName = values[0];
				else
					throw new IllegalArgumentException("VarName not respecting the protocol!");

				double lowerBound = Double.parseDouble(values[1]);
				double upperBound = Double.parseDouble(values[3]);
				double step = Double.parseDouble(values[2]);

				if (step < 0) {
					throw new IllegalArgumentException("Step must be positive!");
				}

				addEntry(varName, lowerBound, upperBound, step);
			} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
				throw new IllegalArgumentException("Request format not respected!");
			}
		}
	}

	//----------------

	public void addEntry(String varName, double lowerBound, double upperBound, double step) {
		variablesValuesTuples.put(varName, generateValues(lowerBound, upperBound, step));
	}

	public double[] generateValues(double lowerBound, double upperBound, double step) {
		double[] values = new double[(int) ((upperBound - lowerBound) / step) + 1];
		int count = 0;

		for (double v = lowerBound; v <= upperBound; v += step) {
			values[count++] = v;
		}

		return values;
	}

	//-----------

	public VariablesTuples getTuples(String valuesKind) throws RuntimeException {
		if (variablesValuesTuples.size() == 0)
			return new VariablesTuples(new String[0], new double[0][0]);

		String[] keysArray = new String[variablesValuesTuples.size()];
		variablesValuesTuples.keySet().toArray(keysArray);

		double[][] result;

		switch (valuesKind) {
			case "GRID" -> {
				result = getCartesianProduct(keysArray);
			}
			case "LIST" -> {
				result = getList(keysArray);
			}
			default -> throw new IllegalArgumentException("Invalid ValuesKind value!");
		}

		return new VariablesTuples(keysArray, result);
	}

	private double[][] getCartesianProduct( String[] keysArray) {
		double[][] tuples = new double[keysArray.length][];

		for (int i = 0; i < keysArray.length; i++) {
			tuples[i] = variablesValuesTuples.get(keysArray[i]);
		}

		//---

		int solutions = 1;
		for (double[] doubles : tuples) {
			solutions *= doubles.length;
		}

		double[][] cartesianProduct = new double[solutions][tuples.length];

		for (int i = 0; i < solutions; i++) {
			int j = 1;
			for (int k = 0; k < tuples.length; k++) {
				double[] tuple = tuples[k];
				cartesianProduct[i][k] = tuple[(i / j) % tuple.length];
				j *= tuple.length;
			}
		}

		return cartesianProduct;
	}

	private double[][] getList(String[] keysArray) throws RuntimeException {
		if (!hasSameLength())
			throw new UnsupportedOperationException("Lists do not have the same length!");

		int length = variablesValuesTuples.get(keysArray[0]).length;

		double[][] tuples = new double[variablesValuesTuples.size()][length];
		variablesValuesTuples.values().toArray(tuples);

		//---

		double[][] outputTuples = new double[length][variablesValuesTuples.size()];

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < variablesValuesTuples.size(); j++) {
				outputTuples[i][j] = tuples[j][i];
			}
		}

		return outputTuples;
	}

	private boolean hasSameLength() {
		int length = -1;

		for (double[] entry : variablesValuesTuples.values()) {
			if (length == -1) {
				length = entry.length;
			} else {
				if (entry.length != length)
					return false;
			}
		}

		return true;
	}

	//--------------


	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		for (String key : variablesValuesTuples.keySet()) {
			out.append("a(").append(key).append(")=(");

			for (double val : variablesValuesTuples.get(key)) {
				out.append(val).append(',');
			}

			out.deleteCharAt(out.length() - 1);
			out.append(")").append(System.lineSeparator());
		}

		return out.toString();
	}
}
