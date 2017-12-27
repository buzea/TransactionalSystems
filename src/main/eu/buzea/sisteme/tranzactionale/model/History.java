package eu.buzea.sisteme.tranzactionale.model;

import java.util.ArrayList;

public class History {

	private final ArrayList<Operation> operations = new ArrayList<>(100);

	public ArrayList<Operation> getOperations() {
		return operations;
	}

	@Override
	public String toString() {
		return "History = " + operations;
	}

}
