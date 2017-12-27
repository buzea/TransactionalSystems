package eu.buzea.sisteme.tranzactionale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import eu.buzea.sisteme.tranzactionale.model.History;
import eu.buzea.sisteme.tranzactionale.model.Operation;

public class HistoryReader {

	public static final String FILENAME = "./history.txt";

	public List<History> readHistories() {
		List<History> result = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				History history = new History();
				String[] operations = currentLine.split(",");
				for (String operationString : operations) {
					Operation operation = new Operation(operationString);
					history.getOperations().add(operation);
				}
				result.add(history);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public History parseHistory(String historyString) {
		History history = new History();
		String[] operations = historyString.split(",");
		for (String operationString : operations) {
			Operation operation = new Operation(operationString);
			history.getOperations().add(operation);
		}
		return history;

	}

}
