package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.util.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schrimpf
 * @created 03.08.2014
 */
public abstract class FrequencyCalculator implements TextScore {
	private final boolean hasHeader;
	private final String valueDelimiter;

	private final Map<String, Long> frequencies;
	private final Pattern pattern;

	protected FrequencyCalculator(final String file, final boolean hasHeader, final String valueDelimiter)
			throws IOException {
		this.hasHeader = hasHeader;
		this.valueDelimiter = valueDelimiter;
		final InputStream resource = getClass().getClassLoader().getResourceAsStream(file);
		if (resource == null)
			throw new IllegalStateException(file + " does not exist");
		this.frequencies = loadFrequencies(resource);
		this.pattern = buildPattern(this.frequencies);
	}

	public double valueOf(String text) {
		text = text.toLowerCase();
		double sum = 0;
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String match = matcher.group();
			long value = frequencies.get(match);
			sum += value;
		}
		return sum;
	}

	protected Map<String, Long> loadFrequencies(final InputStream inputStream) throws IOException {
		/* first run: read file contents into map */
		final String fileContents = FileIO.read(inputStream);
		final StringTokenizer tokenizer = new StringTokenizer(fileContents, FileIO.LINE_SEPARATOR);
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (hasHeader && tokenizer.hasMoreTokens())
			tokenizer.nextToken();
		final Map<String, Long> map = new HashMap<String, Long>();
		while (tokenizer.hasMoreTokens()) {
			final String next = tokenizer.nextToken();
			final String[] parts = next.split(valueDelimiter);
			final String word = parts[0].toLowerCase();
			final long value = Long.valueOf(parts[1]);
			map.put(word, value);
		}
		return map;
	}

	protected Pattern buildPattern(final Map<String, ?> frequencies) {
		String regex = "";
		boolean first = true;
		for (Map.Entry<String, ?> entry : frequencies.entrySet()) {
			if (first) {
				first = false;
			} else {
				regex += "|";
			}
			regex += entry.getKey(); // no need to quote, should be alphanumeric only
		}
		return Pattern.compile(regex);
	}
}
