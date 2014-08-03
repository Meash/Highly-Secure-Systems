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
	private final QgramCalculator qgramCalculator;
	private final Pattern whitespacePattern;

	public EnigmaAnalyzer() throws IOException {
		qgramCalculator = new QgramCalculator();
		whitespacePattern = Pattern.compile("\\s");
	}

	public String findKey(String... ciphertexts) {
		throw new UnsupportedOperationException("not yet implemented");
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
		Enigma encrypter = new Enigma();
		final BestKeyStore bestKey = new BestKeyStore();
		for (int ind1 = 0; ind1 < 26; ind1++) {
			for (int ind2 = 0; ind2 < 26; ind2++) {
				for (int ind3 = 0; ind3 < 26; ind3++) {
					final String key = String.valueOf(Enigma.ALPHABET[ind1]) + Enigma.ALPHABET[ind2] +
							Enigma.ALPHABET[ind3];
					final String plaintext = encrypter.decrypt(ciphertext, key);
					if (!isEncodedProperly(ciphertext, plaintext))
						continue;
					double qgram = qgramCalculator.qgram(plaintext);
					qgram = -qgram;
					bestKey.updateIfBetter(key, qgram);
				}
			}
			System.out.printf("%d/%d complete\n", ind1 + 1, 26);
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
