package eu.buzea.sisteme.tranzactionale;

import java.util.List;

import eu.buzea.sisteme.tranzactionale.model.History;

public class Main {

	public static void main(String[] args) {
		// HistoryGenerator generator = new HistoryGenerator();
		// generator.generateHistory();
		HistoryReader historyReader = new HistoryReader();
		List<History> historyList = historyReader.readHistories();
		for (History history : historyList) {
			System.out.println(history);
			HistoryVerfier verifier = new HistoryVerfier(history);
			System.out.println("Is Valid History: " + verifier.isValid());
			System.out.println("Is in CSR: " + verifier.isCSR());
			System.out.println("Is in OCSR: " + verifier.isOCSR());
			System.out.println("Is in COCSR: " + verifier.isCOCSR());
			System.out.println();
		}
	}
}
