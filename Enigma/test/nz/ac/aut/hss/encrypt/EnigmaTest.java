package nz.ac.aut.hss.encrypt;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Martin Schrimpf
 * @created 04.08.2014
 */
public class EnigmaTest {
	private static final int ALPHABET_SIZE = Enigma.ALPHABET.length;
	private static Enigma enigma;

	@BeforeClass
	public static void setUpBeforeClass() {
		enigma = new Enigma();
	}

	@Test
	public void shortText() {
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		test(plaintext);
	}

	/**
	 * Compares all possible keys to make sure no two create the same ciphertext and can thus decrypt another key's
	 * ciphertext.
	 */
	@Test
	public void sameKeys() {
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		// set up keys
		final String[] keys = new String[Enigma.ALPHABET.length * Enigma.ALPHABET.length * Enigma.ALPHABET.length];
		for (int ind1 = 0; ind1 < ALPHABET_SIZE; ind1++) {
			for (int ind2 = 0; ind2 < ALPHABET_SIZE; ind2++) {
				for (int ind3 = 0; ind3 < ALPHABET_SIZE; ind3++) {
					keys[26 * 26 * ind1 + 26 * ind2 + ind3] = String.valueOf(Enigma.ALPHABET[ind1])
							+ Enigma.ALPHABET[ind2]
							+ Enigma.ALPHABET[ind3];
				}
			}
		}
		for (final String key : keys) {
			assertNotNull(key);
		}
		// compare ciphers
		final String[] ciphers = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			ciphers[i] = enigma.encrypt(plaintext, keys[i]);
			for (int j = 0; j < i; j++) {
				assertNotEquals("Keys " + keys[i] + " and " + keys[j] + " result in the same cipher text", ciphers[i],
						ciphers[j]);
			}
		}
		// cross-over
		for (int i = 0; i < ciphers.length; i++) {
			for (int k = 0; k < i; k++) {
				assertNotEquals("Key " + keys[k] + " can decrypt the plaintext of " + keys[i], plaintext,
						enigma.decrypt(ciphers[i], keys[k]));
			}
		}
	}

	/**
	 * Tests all different keys.
	 * @param plaintext the plaintext to test
	 */
	private void test(final String plaintext) {
		for (int ind1 = 0; ind1 < ALPHABET_SIZE; ind1++) {
			for (int ind2 = 0; ind2 < ALPHABET_SIZE; ind2++) {
				for (int ind3 = 0; ind3 < ALPHABET_SIZE; ind3++) {
					final String key = String.valueOf(Enigma.ALPHABET[ind1])
							+ Enigma.ALPHABET[ind2]
							+ Enigma.ALPHABET[ind3];
					final String ciphertext = enigma.encrypt(plaintext, key);
					assertEquals(plaintext, enigma.decrypt(ciphertext, key));
				}
			}
		}
	}
}
