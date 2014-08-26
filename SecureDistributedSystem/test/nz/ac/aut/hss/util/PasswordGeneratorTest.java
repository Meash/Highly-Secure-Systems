package nz.ac.aut.hss.util;

import nz.ac.aut.hss.distribution.util.PasswordGenerator;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PasswordGeneratorTest {
	@Test
	public void stringPassword() {
		for (int i = 0; i < 100; i++) {
			final int maxLength = 8;
			final int minLength = 6;
			final String pass = PasswordGenerator.generateRandomPassword(minLength, maxLength);
			assertTrue(pass.length() >= minLength && pass.length() <= maxLength);
		}
	}

	@Test
	public void aes() throws NoSuchAlgorithmException {
		assertNotNull(PasswordGenerator.generateSecretKey("AES"));
	}
}
