package eu.buzea.sisteme.tranzactionale;

import static eu.buzea.sisteme.tranzactionale.HistoryReader.FILENAME;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class HistoryGenerator {

	public void generateHistories(int numberOfHistories) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILENAME), "utf-8"))) {
			writer.write("r1(x),w2(y),c1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
