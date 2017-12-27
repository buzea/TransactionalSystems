package eu.buzea.sisteme.tranzactionale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import eu.buzea.sisteme.tranzactionale.model.History;
import eu.buzea.sisteme.tranzactionale.model.Operation;
import eu.buzea.sisteme.tranzactionale.model.OperationType;

public class PrecedenceFinder {

	private final History				history;

	private final Map<Integer, Boolean>	hasBegun;

	public PrecedenceFinder(History history) {
		this.history = history;
		hasBegun = new HashMap<>();
	}

	public Set<Pair<Integer, Integer>> computeTrasactionPrecedences() {
		Set<Pair<Integer, Integer>> result = new HashSet<>();
		ArrayList<Operation> operations = history.getOperations();
		for (int i = 0; i < operations.size(); i++) {
			Operation operation1 = operations.get(i);
			int transactionNumber1 = operation1.getTransactionNumber();
			if (!hasBegun(transactionNumber1)) {
				hasBegun.put(transactionNumber1, true);
				for (int j = i - 1; j > 0; j--) {
					Operation operation2 = operations.get(j);
					if (operation2.getType() == OperationType.COMMIT) {
						ImmutablePair<Integer, Integer> immutablePair = new ImmutablePair<>(operation2.getTransactionNumber(),
								transactionNumber1);
						result.add(immutablePair);
					}
				}
			}
		}

		return result;
	}

	private boolean hasBegun(int transactionNumber) {
		return Boolean.TRUE.equals(hasBegun.get(transactionNumber));
	}

	public List<Integer> computeCommitPrecedences() {
		ArrayList<Operation> operations = history.getOperations();
		//@formatter:off
		return operations.stream()
				.filter(operation -> operation.getType() == OperationType.COMMIT)
				.map(operation -> operation.getTransactionNumber())
				.collect(Collectors.toList());
		//@formatter:on
	}

}
