package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alessio Manià - IN0500908
 */
public class VariableValuesFunction {
	private final Map<String, List<Double>> variablesValuesTuples;                    //NOTA! MANTIENE ORDINE!

	public VariableValuesFunction(String variableValuesFunctionString) throws InvalidRequestException {
		this.variablesValuesTuples = new LinkedHashMap<>();
		parseFunction(variableValuesFunctionString);
	}

	//----------------

	private void parseFunction(String function) throws InvalidRequestException {
		String[] variablesValues = function.split(",");

		for (String variablesValue : variablesValues) {
			String[] values = variablesValue.split(":");

			try {
				String varName;
				if (values[0].matches("[a-z][a-z0-9]*"))
					varName = values[0];
				else
					throw new InvalidRequestException("Unable to process VariableValuesFunction: VarName not respecting the protocol!");

				double lowerBound = Double.parseDouble(values[1]);
				double upperBound = Double.parseDouble(values[3]);
				double step = Double.parseDouble(values[2]);

				if (step < 0) {
					throw new InvalidRequestException("Unable to process VariableValuesFunction: step must be positive!");
				}

				variablesValuesTuples.put(varName, generateValues(lowerBound, upperBound, step));
			} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
				throw new InvalidRequestException("Unable to process VariableValuesFunction", ex);
			}
		}
	}

	//----------------

	public List<Double> generateValues(double lowerBound, double upperBound, double step) {
		BigDecimal bdLowerBound = new BigDecimal(lowerBound);								//Utilizzo di BigDecimal per migliorare precisione
		BigDecimal bdStep = new BigDecimal(step);
		BigDecimal bdUpperBound = new BigDecimal(upperBound).add(bdStep);

		return Stream.iterate(bdLowerBound, d -> d.compareTo(bdUpperBound) <0, d -> d.add(bdStep)).mapToDouble(BigDecimal::doubleValue).boxed().collect(Collectors.toList());
	}

	//-----------

	public VariablesTuples getTuples(String valuesKind) throws InvalidRequestException {
		if (variablesValuesTuples.size() == 0)
			return new VariablesTuples(new ArrayList<>(), new ArrayList<>());

		List<String> keysList = new ArrayList<>(variablesValuesTuples.keySet());		//necessario per accedere a i-mo elemento

		List<List<Double>> result;

		switch (valuesKind) {
			case "GRID" -> {
				result = getCartesianProduct(keysList);
			}
			case "LIST" -> {
				result = getList(keysList);
			}
			default -> throw new InvalidRequestException("Invalid ValuesKind value!");
		}

		return new VariablesTuples(keysList, result);
	}

	private List<List<Double>> getCartesianProduct( List<String> keysList) {
		List<List<Double>> tuples = new ArrayList<>();

		for (String s : keysList) {
			tuples.add(variablesValuesTuples.get(s));
		}

		//---

		int solutions = 1;
		for (List<Double> tuple : tuples) {
			solutions *= tuple.size();
		}

		List<List<Double>> cartesianProduct = new ArrayList<>();

		for (int i = 0; i < solutions; i++) {
			int j = 1;
			List<Double> list = new ArrayList<>();

			for (List<Double> tuple : tuples) {
				list.add(tuple.get((i / j) % tuple.size()));
				j *= tuple.size();
			}

			cartesianProduct.add(list);
		}

		return cartesianProduct;
	}

	private List<List<Double>> getList(List<String> keysList) throws InvalidRequestException {
		if (!hasSameLength())
			throw new InvalidRequestException("Lists do not have the same length!");

		int length = variablesValuesTuples.get(keysList.get(0)).size();

		List<List<Double>> tuples = new ArrayList<>(variablesValuesTuples.values());

		//---

		List<List<Double>> outputTuples = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			List<Double> list = new ArrayList<>();
			for (int j = 0; j < variablesValuesTuples.size(); j++) {
				list.add(tuples.get(j).get(i));
			}

			outputTuples.add(list);
		}

		return outputTuples;
	}

	private boolean hasSameLength() {
		int length = -1;

		for (List<Double> entry : variablesValuesTuples.values()) {
			if (length == -1) {
				length = entry.size();
			} else {
				if (entry.size() != length)
					return false;
			}
		}

		return true;
	}

	//--------------

	@Override
	public String toString() {
		return variablesValuesTuples.toString();
	}
}
