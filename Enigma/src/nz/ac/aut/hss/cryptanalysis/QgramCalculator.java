package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.util.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates quadgram statistics of text in the english language.
 * @author Martin Schrimpf
 * @created 03.08.2014
 * @see <a href="http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams">Quadgram Statistics as
 * a Fitness Measure</a>
 */
public class QgramCalculator {
	private static final String VALUE_DELIMITER = "\t";
	private static final int MIN_LENGTH = 3;

	private final Map<String, Float> frequencies;
	private final Pattern pattern;

	public QgramCalculator() throws IOException {
		final String file = "frequencies.txt";
		final InputStream resource = getClass().getClassLoader().getResourceAsStream(file);
		if (resource == null)
			throw new IllegalStateException(file + " does not exist");
		this.frequencies = loadFrequencies(resource);
		this.pattern = buildPattern(this.frequencies);
	}

	public double valueOf(String text) {
		double sum = 0;
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String match = matcher.group();
			float value = frequencies.get(match);
			sum += value;
		}
		return sum;
	}

	private Map<String, Float> loadFrequencies(final InputStream inputStream) throws IOException {
		/* first run: read file contents into map */
		final String fileContents = FileIO.read(inputStream);
		final StringTokenizer tokenizer = new StringTokenizer(fileContents, FileIO.LINE_SEPARATOR);
		final Map<String, Float> map = new HashMap<String, Float>();
		BigInteger totalCount = BigInteger.valueOf(0);
		while (tokenizer.hasMoreTokens()) {
			final String next = tokenizer.nextToken();
			final String[] parts = next.split(VALUE_DELIMITER);
			final String word = parts[0];
			if(word.length() < MIN_LENGTH)
				continue;
			final long value = Long.valueOf(parts[1]);
			map.put(word, (float) value);

			totalCount = totalCount.add(BigInteger.valueOf(value));
		}

		/* second run: adjust values */
		Iterator<Map.Entry<String, Float>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, Float> entry = iterator.next();
			final float value = BigInteger.valueOf((long) ((float) entry.getValue())).divide(totalCount).floatValue();
			if(value == 0)
				iterator.remove();
			else
				map.put(entry.getKey(), value);
		}
		return map;
	}


	private Pattern buildPattern(final Map<String, Float> frequencies) {
		String regex = "";
		boolean first = true;
		for (Map.Entry<String, Float> entry : frequencies.entrySet()) {
			if (first) {
				first = false;
			} else {
				regex += "|";
			}
			regex += entry.getKey().toUpperCase(); // no need to quote, should be alphanumeric only
		}
		return Pattern.compile(regex);
	}

}
