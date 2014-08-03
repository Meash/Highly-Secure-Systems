package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.util.FileIO;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Calculates quadgram statistics of text in the english language.
 * @author Martin Schrimpf
 * @created 03.08.2014
 * @see <a href="http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams">Quadgram Statistics as
 * a Fitness Measure</a>
 */
public class QgramIndexCalculator implements TextScore {
	private static final String VALUE_DELIMITER = ",";
	// qgram obtained from http://www.practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
	private final float qgram[];

	public QgramIndexCalculator() throws IOException {
		final String file = "qgram.txt";
		final InputStream resource = getClass().getClassLoader().getResourceAsStream(file);
		if (resource == null)
			throw new IllegalStateException(file + " does not exist");
		qgram = loadQgram(resource);
	}

	public double valueOf(String text) {
		final char[] charText = text.toCharArray();
		int temp[] = new int[4];
		double score = 0;
		for (int i = 0; i < charText.length - 3; i++) {
			temp[0] = charText[i] - 'A';
			temp[1] = charText[i + 1] - 'A';
			temp[2] = charText[i + 2] - 'A';
			temp[3] = charText[i + 3] - 'A';
			final int index = 17576 * temp[0] + 676 * temp[1] + 26 * temp[2] + temp[3];
			score += qgram[index];
		}
		return -score;
	}


	private float[] loadQgram(final InputStream inputStream) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(FileIO.read(inputStream), VALUE_DELIMITER);
		List<Float> result = new ArrayList<Float>();
		while (tokenizer.hasMoreTokens()) {
			String next = tokenizer.nextToken();
			float value = Float.valueOf(next);
			result.add(value);
		}
		final Float[] resultArray = result.toArray(new Float[result.size()]);
		return ArrayUtils.toPrimitive(resultArray);
	}
}
