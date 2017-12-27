package eu.buzea.sisteme.tranzactionale;

import java.util.ArrayList;
import java.util.List;

public class ToposortResult {

	private List<Integer> order;

	public ToposortResult() {
		setOrder(new ArrayList<>());
	}

	public ToposortResult(int element) {
		this();
		getOrder().add(element);
	}

	public void addAsHead(Integer e) {
		ArrayList<Integer> newOrder = new ArrayList<>();
		newOrder.add(e);
		newOrder.addAll(getOrder());
		setOrder(newOrder);
	}

	@Override
	public String toString() {
		return "ToposortResult =\n" + this.getOrder();
	}

	public List<Integer> getOrder() {
		return order;
	}

	private void setOrder(List<Integer> order) {
		this.order = order;
	}

}
