package nz.ac.aut.hss.util;

import nz.ac.aut.hss.distribution.util.PasswordGenerator;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PasswordGeneratorTest {
	@Test
	public void aes() throws NoSuchAlgorithmException {
		assertNotNull(PasswordGenerator.generateSecretKey("AES"));
	}
}
