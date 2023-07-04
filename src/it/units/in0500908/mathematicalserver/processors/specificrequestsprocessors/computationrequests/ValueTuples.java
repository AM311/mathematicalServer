package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors.computationrequests;

import java.util.*;

/**
 * @author Alessio Manià - IN0500908
 */
public class ValueTuples {
	private final Map<String, Integer> variablesPositions;                  //Codifica nomi e posizioni relative
	private final List<List<Double>> tuples;                                //List<List> e non Set<List> per coerenza con la notazione della consegna

	public ValueTuples(List<String> varNames, List<List<Double>> tuples) throws IllegalArgumentException {
		if (!isValid(varNames, tuples))
			throw new IllegalArgumentException("Impossible to create a Tuple: invalid arguments!");

		this.tuples = tuples;

		variablesPositions = new LinkedHashMap<>();                    //mantiene ordine + anche parametri valorizzati da VVF con LinkedHash
		for (int i = 0; i < varNames.size(); i++) {
			variablesPositions.put(varNames.get(i), i);
		}
	}

	private boolean isValid(List<String> varNames, List<List<Double>> tuples) {
		int size = -1;

		for (List<Double> tuple : tuples) {
			if (size == -1) {
				size = tuple.size();
			} else {
				if (tuple.size() != size)
					return false;
			}
		}

		return varNames.size() == size;
	}

	//---------

	public Map<String, Double> getTupleByIndex(int index) throws IndexOutOfBoundsException {
		if (index > getNumOfTuples()) {
			throw new IndexOutOfBoundsException("There are less then " + index + " tuples!");
		}

		Map<String, Double> iTuple = new HashMap<>();

		for (String varName : variablesPositions.keySet()) {                                            //Ordine è garantito: LinkedHashMap --> SortedSet in runtime
			iTuple.put(varName, tuples.get(index).get(variablesPositions.get(varName)));
		}

		return iTuple;
	}

	public int getNumOfTuples() {
		return tuples.size();
	}

	//---------

	@Override
	public int hashCode() {
		return Objects.hash(variablesPositions, tuples);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ValueTuples that))
			return false;
		return Objects.equals(variablesPositions, that.variablesPositions) && Objects.equals(tuples, that.tuples);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append("T(");
		for (String key : variablesPositions.keySet()) {
			out.append(key).append(",");
		}
		out.deleteCharAt(out.length() - 1);
		out.append(")=(");

		for (List<Double> valTuple : tuples) {
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
