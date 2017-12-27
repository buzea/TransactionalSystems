package eu.buzea.sisteme.tranzactionale.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operation {
	private final Pattern		pattern	= Pattern.compile("(r|w|c)([0-9])(\\(([a-z])\\))?");
	private final OperationType	type;
	private final int			transactionNumber;
	private final String		variable;

	public Operation(String stringRepresentation) {
		Matcher matcher = pattern.matcher(stringRepresentation);
		if (!matcher.matches()) {
			throw new UnsupportedOperationException();
		}

		switch (matcher.group(1)) {
			case "r":
				type = OperationType.READ;
				break;
			case "w":
				type = OperationType.WRITE;
				break;
			case "c":
				type = OperationType.COMMIT;
				break;
			default:
				throw new UnsupportedOperationException();
		}

		transactionNumber = Integer.parseInt(matcher.group(2));
		if (matcher.groupCount() == 4) {
			variable = matcher.group(4);
		} else {
			variable = null;
		}

	}

	public String getVariable() {
		return variable;
	}

	public OperationType getType() {
		return type;
	}

	public int getTransactionNumber() {
		return transactionNumber;
	}

	@Override
	public String toString() {
		return "" + type + transactionNumber + (variable != null ? "(" + variable + ")" : "");
	}

}
