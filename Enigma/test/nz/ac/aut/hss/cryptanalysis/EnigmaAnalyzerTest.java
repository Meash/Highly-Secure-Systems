package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.encrypt.Enigma;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 31.07.2014
 */
public class EnigmaAnalyzerTest {
	private static EnigmaAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		analyzer = new EnigmaAnalyzer();
	}

	@Test
	public void shortCipherText() throws Exception {
		final String plaintext =
				"intelligencepointstoattackontheeastwallofthecastleatdawn";
		final Enigma enigma = new Enigma();
		String ciphertext = enigma.encrypt(plaintext, "MAS");
		System.out.printf("%7s: %s\n", "Cipher", ciphertext);
		String key = analyzer.findKey(ciphertext);
		System.out.printf("%7s: %s\n", "Key", key);
		String analyzedPlaintext = enigma.decrypt(ciphertext, key);
		System.out.printf("%7s: %s\n", "Plain", analyzedPlaintext);
		assertEquals(plaintext, analyzedPlaintext);
	}
}
