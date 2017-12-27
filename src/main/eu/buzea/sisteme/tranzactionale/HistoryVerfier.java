package eu.buzea.sisteme.tranzactionale;

import static eu.buzea.sisteme.tranzactionale.model.OperationType.COMMIT;
import static eu.buzea.sisteme.tranzactionale.model.OperationType.WRITE;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import eu.buzea.sisteme.tranzactionale.model.History;
import eu.buzea.sisteme.tranzactionale.model.Operation;

public class HistoryVerfier {

	private final History				history;

	private Map<Integer, List<Integer>>	conflictGraph	= new HashMap<>();

	private Map<Integer, Color>			colors			= new HashMap<>();

	private Boolean						csr;

	private Boolean						ocsr;

	private Boolean						cocsr;

	private Integer						numberOfTransactions;

	private List<ToposortResult>		computedToposorts;

	private PrecedenceFinder			precedenceFinder;

	public HistoryVerfier(History history) {
		this.history = history;
		precedenceFinder = new PrecedenceFinder(history);
		csr = null;
		ocsr = null;
		cocsr = null;
		numberOfTransactions = 0;
	}

	/**
	 * Verify if the history is valid. We check that:
	 * <ol>
	 * <li>There are no operations after the commit</li>
	 * </ol>
	 *
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		ArrayList<Operation> operations = history.getOperations();
		for (int i = 0; i < operations.size(); i++) {
			Operation operation = operations.get(i);
			numberOfTransactions = Math.max(operation.getTransactionNumber(), numberOfTransactions);
			if (operation.getType() == COMMIT) {
				for (int j = i + 1; j < operations.size(); j++) {
					Operation followingOperation = operations.get(j);
					if (followingOperation.getTransactionNumber() == operation.getTransactionNumber()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the history is commit serializable
	 *
	 * @return
	 */
	public boolean isCSR() {
		if (csr == null) {
			ArrayList<Operation> operations = history.getOperations();
			for (int i = 0; i < operations.size(); i++) {
				Operation first = operations.get(i);
				for (int j = i + 1; j < operations.size(); j++) {
					Operation second = operations.get(j);
					if (isConflict(first, second)) {
						addConflict(first, second);
					}
				}

			}
			csr = !isCycleInConflictGraph();
		}
		return csr;
	}

	private void addConflict(Operation first, Operation second) {
		int t1 = first.getTransactionNumber();
		int t2 = second.getTransactionNumber();
		List<Integer> adjacencyList = conflictGraph.computeIfAbsent(t1, ignoreThis -> new ArrayList<>());
		adjacencyList.add(t2);
	}

	private boolean isCycleInConflictGraph() {
		for (int i = 1; i <= numberOfTransactions; i++) {
			colors.put(i, Color.WHITE);// and nerdy
		}

		for (Integer vertex : conflictGraph.keySet()) {
			if (!isVisited(vertex)) {
				if (visitComponent(vertex)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean visitComponent(Integer transactionNumber) {
		colors.put(transactionNumber, Color.GRAY);
		if (conflictGraph.containsKey(transactionNumber)) {
			for (Integer secondTransactionNumber : conflictGraph.get(transactionNumber)) {
				if (colors.get(secondTransactionNumber).equals(Color.GRAY)) {
					return true;
				}
				if (colors.get(secondTransactionNumber).equals(Color.WHITE)) {
					if (visitComponent(secondTransactionNumber)) {
						return true;
					}
				}

			}
		}
		colors.put(transactionNumber, Color.BLACK);
		return false;
	}

	private boolean isVisited(int transactionNumber) {
		return !Color.WHITE.equals(colors.get(transactionNumber));
	}

	private boolean isConflict(Operation first, Operation second) {
		if (first.getTransactionNumber() != second.getTransactionNumber()) {
			if (first.getType() == WRITE || second.getType() == WRITE) {
				if (first.getVariable() != null && first.getVariable().equals(second.getVariable())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isOCSR() {
		if (ocsr == null) {
			ocsr = false;
			if (isCSR()) {
				Set<Pair<Integer, Integer>> precedences = precedenceFinder.computeTrasactionPrecedences();
				TopologicalSorter topoSorter = new TopologicalSorter(conflictGraph, numberOfTransactions);
				computedToposorts = topoSorter.computeAllToposorts();
				for (ToposortResult toposort : computedToposorts) {
					if (respectsPrecedences(toposort, precedences)) {
						ocsr = true;
						break;
					}
				}
			}

		}
		return ocsr;
	}

	private boolean respectsPrecedences(ToposortResult toposort, Set<Pair<Integer, Integer>> precedences) {
		List<Integer> order = toposort.getOrder();
		for (Pair<Integer, Integer> precedence : precedences) {
			Integer firstTransaction = precedence.getKey();
			Integer secondTransaction = precedence.getValue();
			for (int i = 0; i < order.size(); i++) {
				if (order.get(i) == secondTransaction) {
					for (int j = i + 1; j < order.size(); j++) {
						if (order.get(j) == firstTransaction) {
							return false;
						}
					}
				}

			}
		}
		return true;
	}

	public boolean isCOCSR() {
		if (cocsr == null) {
			cocsr = false;
			if (isCSR()) {
				List<Integer> commitPrecedences = precedenceFinder.computeCommitPrecedences();
				for (ToposortResult topsort : computedToposorts) {
					if (topsort.getOrder().equals(commitPrecedences)) {
						cocsr = true;
						break;
					}
				}
			}

		}
		return cocsr;
	}
}
