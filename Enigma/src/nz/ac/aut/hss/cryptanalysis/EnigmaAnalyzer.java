package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.encrypt.Enigma;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public class EnigmaAnalyzer implements CryptAnalyzer {
	private static final boolean DEBUG = false;

	private final TextScore textScore;
	private final Pattern whitespacePattern;
	private static final int ALPHABET_SIZE = Enigma.ALPHABET.length;

	public EnigmaAnalyzer() throws IOException {
		textScore = new BigramCalculator();
		whitespacePattern = Pattern.compile("\\s");
	}

	/**
	 * Attempts to find the key of the given cipher text.
	 * Assumes a 3-roto enigma machine.
	 * @param ciphertext the encrypted text
	 * @return the key used to generate the given ciphertext or null if no key could be found
	 */
	public String findKey(String ciphertext) {
		if (!StringUtils.isAllUpperCase(ciphertext))
			throw new IllegalArgumentException("Ciphertext is not all upper-case");
		final Enigma encrypter = new Enigma();
		final BestKeyStore bestKey = new BestKeyStore();
		for (int ind1 = 0; ind1 < ALPHABET_SIZE; ind1++) {
			for (int ind2 = 0; ind2 < ALPHABET_SIZE; ind2++) {
				for (int ind3 = 0; ind3 < ALPHABET_SIZE; ind3++) {
					final String key = String.valueOf(Enigma.ALPHABET[ind1])
							+ Enigma.ALPHABET[ind2]
							+ Enigma.ALPHABET[ind3];
					final String plaintext = encrypter.decrypt(ciphertext, key);
					if (!isEncodedProperly(ciphertext, plaintext))
						continue;
					final double score = textScore.valueOf(plaintext);
					bestKey.updateIfBetter(key, score);
				}
			}
			debug("%2.0f%% ", (float) (ind1 + 1) / ALPHABET_SIZE * 100);
		}
		debug("\n");
		return bestKey.getBestKey();
	}

	private void debug(final String format, final Object... args) {
		if (DEBUG) {
			System.out.printf(format, args);
		}
	}

	private boolean isEncodedProperly(final String ciphertext, final String plaintext) {
		return ciphertext.length() == plaintext.length()
				&& !containsWhitespace(ciphertext, plaintext)
				&& isAnyCharacterEncodedToSame(ciphertext, plaintext);
	}

	private boolean containsWhitespace(final String ciphertext, final String plaintext) {
		final String[] texts = {ciphertext, plaintext};
		for (String text : texts) {
			Matcher matcher = whitespacePattern.matcher(text);
			if (matcher.find())
				return true;
		}
		return false;
	}

	private boolean isAnyCharacterEncodedToSame(final String ciphertext, final String plaintext) {
		for (int i = 0; i < ciphertext.length(); i++) {
			if (ciphertext.charAt(i) != plaintext.charAt(i))
				return true;
		}
		return false;
	}
}
