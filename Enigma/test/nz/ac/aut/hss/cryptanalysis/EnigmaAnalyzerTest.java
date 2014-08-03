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
		System.out.print("Setting up...");
		System.out.flush();
		analyzer = new EnigmaAnalyzer();
		System.out.println(" complete.");
	}

	@Test
	public void shortCipherText() throws Exception {
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		test(plaintext, "MAS");
	}

	@Test
	public void greeting() throws Exception {
		final String plaintext = "togeneraloberzalekxnothingtoreport";
		test(plaintext, "MAS");
	}

	private void test(final String plaintext, final String key) {
		final Enigma enigma = new Enigma();
		final String ciphertext = enigma.encrypt(plaintext, key);
		System.out.printf("%15s: %s\n", "Cipher", ciphertext);
		final String analyzedKey = analyzer.findKey(ciphertext);
		System.out.printf("%15s: %s\n", "Analyzed Key", analyzedKey);
		assertEquals(key, analyzedKey);
		final String analyzedPlaintext = enigma.decrypt(ciphertext, analyzedKey);
		System.out.printf("%15s: %s\n", "Analyzed Plain", analyzedPlaintext);
		assertEquals(plaintext, analyzedPlaintext);
	}
}
