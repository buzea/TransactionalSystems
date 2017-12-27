package eu.buzea.sisteme.tranzactionale.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.buzea.sisteme.tranzactionale.TopologicalSorter;
import eu.buzea.sisteme.tranzactionale.ToposortResult;

public class TestTopologicaSort {

	private TopologicalSorter sorter;

	@Test
	public void testMultipleEntryDag() {
		Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
		adjacencyList.put(1, Arrays.asList(3));
		adjacencyList.put(2, Arrays.asList(3));
		adjacencyList.put(3, Arrays.asList(4));
		adjacencyList.put(4, Arrays.asList(5, 6));
		adjacencyList.put(5, Arrays.asList(7));
		adjacencyList.put(6, Arrays.asList(7));
		int numberOfTransactions = 7;
		sorter = new TopologicalSorter(adjacencyList, numberOfTransactions);
		List<ToposortResult> allToposorts = sorter.computeAllToposorts();
		assertEquals(4, allToposorts.size());
		System.out.println(allToposorts);

	}

}
