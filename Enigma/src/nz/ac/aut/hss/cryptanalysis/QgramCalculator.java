package nz.ac.aut.hss.cryptanalysis;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Martin Schrimpf
 * @created 03.08.2014
 */
public class QgramCalculator {
	private static final String VALUE_DELIMITER = ",";
	// qgram obtained from http://www.practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
	private final float qgram[];

	public QgramCalculator() throws IOException {
		final String file = "qgram.txt";
		final InputStream resource = getClass().getClassLoader().getResourceAsStream(file);
		if(resource == null)
			throw new IllegalStateException(file + " does not exist");
		qgram = loadQgram(resource);
	}

	private float[] loadQgram(final InputStream inputStream) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(read(inputStream), VALUE_DELIMITER);
		List<Float> result = new ArrayList<Float>();
		while (tokenizer.hasMoreTokens()) {
			String next = tokenizer.nextToken();
			float value = Float.valueOf(next);
			result.add(value);
		}
		final Float[] resultArray = result.toArray(new Float[result.size()]);
		return ArrayUtils.toPrimitive(resultArray);
	}


	public double qgram(String text) {
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
		return score;
	}

	/**
	 * Efficiently reads the content of a File.
	 * @param inputStream the input
	 * @return the String-content of the File
	 * @throws IOException           when the file could not be read
	 * @throws FileNotFoundException when the file does not exist
	 */
	public static String read(InputStream inputStream)
			throws IllegalArgumentException, IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder result = new StringBuilder();
			while((line = reader.readLine()) != null)
				result.append(line);
			return result.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

}
