package nz.ac.aut.hss.encrypt;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractEnigmaTest {
	private static final int ALPHABET_SIZE = Enigma.ALPHABET.length;
	private static Enigma enigma;

	@Before
	public  void setUp() {
		enigma = createMachine(3);
	}

	@Test
	public void encryptDecrypt() {
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		final String key = "MAS";
		final String ciphertext = enigma.encrypt(plaintext, key);
		System.out.printf("%7s: %s", "Cipher", ciphertext);
		assertTrue(StringUtils.isAlpha(ciphertext));
		assertEquals(plaintext, enigma.decrypt(ciphertext, key));
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
					keys[ALPHABET_SIZE * ALPHABET_SIZE * ind1 + ALPHABET_SIZE * ind2 + ind3] =
							String.valueOf(Enigma.ALPHABET[ind1])
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

	public abstract Enigma createMachine(final int rotors);
}
