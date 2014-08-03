package nz.ac.aut.hss.cryptanalysis;

import java.io.IOException;

/**
 * Calculates quadgram statistics of text in the english language.
 * @author Martin Schrimpf
 * @created 03.08.2014
 * @see <a href="http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams">Quadgram Statistics as
 * a Fitness Measure</a>
 */
public class BigramCalculator extends FrequencyCalculator {
	public BigramCalculator() throws IOException {
		super("bigrams.csv", true, ",");
	}
}
