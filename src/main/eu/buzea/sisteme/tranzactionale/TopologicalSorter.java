package eu.buzea.sisteme.tranzactionale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologicalSorter {

	private final Map<Integer, List<Integer>>	adjacencyList;

	private final Map<Integer, Integer>			indegree;

	private final Map<Integer, Boolean>			visited;

	private final int							numberOfTransactions;

	public TopologicalSorter(Map<Integer, List<Integer>> adjacencyList, int numberOfTransactions) {
		this.adjacencyList = adjacencyList;
		this.numberOfTransactions = numberOfTransactions;
		visited = new HashMap<>();
		indegree = new HashMap<>(numberOfTransactions + 2);
		for (int i = 1; i <= numberOfTransactions; i++) {
			indegree.put(i, 0);
		}
		for (List<Integer> list : adjacencyList.values()) {
			for (Integer transaction : list) {
				indegree.computeIfPresent(transaction, (key, oldValue) -> oldValue + 1);
			}
		}
	}

	public List<ToposortResult> computeAllToposorts() {
		List<ToposortResult> result = new ArrayList<>();
		for (int i = 1; i <= numberOfTransactions; i++) {
			if (!isVisited(i) && 0 == indegree.get(i)) {
				mark(i);
				List<ToposortResult> partialResults = computeAllToposorts();
				if (partialResults.isEmpty()) {
					result.add(new ToposortResult(i));
				} else {
					for (ToposortResult partialResult : partialResults) {
						partialResult.addAsHead(i);
						result.add(partialResult);
					}
				}
				unmark(i);

			}
		}
		return result;
	}

	private void unmark(int i) {
		visited.put(i, false);
		if (adjacencyList.containsKey(i)) {
			for (Integer neighbor : adjacencyList.get(i)) {
				indegree.computeIfPresent(neighbor, (key, oldValue) -> oldValue + 1);
			}
		}

	}

	private void mark(int i) {
		visited.put(i, true);
		if (adjacencyList.containsKey(i)) {
			for (Integer neighbor : adjacencyList.get(i)) {
				indegree.computeIfPresent(neighbor, (key, oldValue) -> oldValue - 1);
			}
		}
	}

	private boolean isVisited(int transactionNumber) {
		return Boolean.TRUE.equals(visited.get(transactionNumber));
	}
}
