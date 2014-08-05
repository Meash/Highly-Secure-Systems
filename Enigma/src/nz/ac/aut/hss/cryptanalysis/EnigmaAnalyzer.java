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
	private final TextScore textScore;
	private final Pattern whitespacePattern;
	private static final int ALPHABET_SIZE = Enigma.ALPHABET.length;
	private final int rotors;

	public EnigmaAnalyzer(final int rotors) throws IOException {
		this.rotors = rotors;
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
		// Search all possible keys by utilizing maths instead of nested for loops
		// (this also allows a variable amount of rotors).
		// The total amount of possible keys is ALPHABET_SIZE^rotors, our max value.
		// Each integer value from zero to this max value represents one unique key
		// that can be determined by consecutively performing modulo operations on the integer value
		// and "cutting off" the just used bits with a division.
		final int possibleKeys = (int) Math.pow(ALPHABET_SIZE, rotors);
		for (int num = 0; num < possibleKeys; num++) {
			String key = "";
			for (int k = 0; k < rotors; k++) {
				final int index = (int) (num / Math.pow(ALPHABET_SIZE, k)) % ALPHABET_SIZE;
				key = String.valueOf(Enigma.ALPHABET[index]) + key; // make order AAA, AAB... instead of AAA, BAA...
			}
			final String plaintext = encrypter.decrypt(ciphertext, key);
			// save some score computing time by validating the plaintext
			if (!isEncodedProperly(ciphertext, plaintext))
				continue;
			final double score = textScore.valueOf(plaintext);
			bestKey.updateIfBetter(key, score);
		}
		return bestKey.getBestKey();
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
