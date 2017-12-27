package eu.buzea.sisteme.tranzactionale.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.buzea.sisteme.tranzactionale.HistoryReader;
import eu.buzea.sisteme.tranzactionale.HistoryVerfier;
import eu.buzea.sisteme.tranzactionale.model.History;

public class TestHistoryVerifier {

	@Test
	public void testCsr() {
		HistoryReader historyReader = new HistoryReader();
		History history = historyReader.parseHistory("r2(x),w2(x),r1(x),r1(y),r2(y),w2(y),c1,c2");
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertFalse(verifier.isCSR());
		assertFalse(verifier.isOCSR());
		assertFalse(verifier.isCOCSR());
	}

	@Test
	public void testOcsrFalse() {
		HistoryReader historyReader = new HistoryReader();
		History history = historyReader.parseHistory("w1(x),r2(x),c2,w3(y),c3,w1(y),c1");
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertTrue(verifier.isCSR());
		assertFalse(verifier.isOCSR());
		assertFalse(verifier.isCOCSR());
	}

	@Test
	public void testOcsrTrue() {
		HistoryReader historyReader = new HistoryReader();
		History history = historyReader.parseHistory("r1(x),r2(x),r1(z),w1(x),w2(y),r3(z),w3(y),c1,c2,w3(z),c3");
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertTrue(verifier.isCSR());
		assertTrue(verifier.isOCSR());
		assertFalse(verifier.isCOCSR());
	}

	@Test
	public void testCocsrFalse() {
		HistoryReader historyReader = new HistoryReader();
		History history = historyReader.parseHistory("w3(y),c3,w1(x),r2(x),c2,w1(y),c1");
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertTrue(verifier.isCSR());
		assertTrue(verifier.isOCSR());
		assertFalse(verifier.isCOCSR());
	}

	@Test
	public void testCocsrTrue() {
		HistoryReader historyReader = new HistoryReader();
		History history = historyReader.parseHistory("w3(y),c3,w1(x),r2(x),w1(y),c1,c2");
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertTrue(verifier.isCSR());
		assertTrue(verifier.isOCSR());
		assertTrue(verifier.isCOCSR());
	}

	@Test
	public void testSerialSchedule() {
		HistoryReader historyReader = new HistoryReader();
		//@formatter:off
		String historyString = "r1(x),w1(x),r1(y),r1(z),w1(q),c1," +
							   "r2(x),w2(x),r2(q),w2(z),w2(y),c2," +
							   "r3(x),w3(x),r3(q),w3(z),w3(y),c3";
		//@formatter:on
		History history = historyReader.parseHistory(historyString);
		HistoryVerfier verifier = new HistoryVerfier(history);
		assertTrue(verifier.isValid());
		assertTrue(verifier.isCSR());
		assertTrue(verifier.isOCSR());
		assertTrue(verifier.isCOCSR());
	}
}
