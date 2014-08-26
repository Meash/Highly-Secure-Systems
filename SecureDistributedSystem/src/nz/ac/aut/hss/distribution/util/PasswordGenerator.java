package nz.ac.aut.hss.distribution.util;

import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class PasswordGenerator {
	public static String generateRandomPassword(final int minLength, final int maxLength) {
		final int passwordLength = (int) (minLength + Math.random() * (maxLength - minLength));
		return RandomStringUtils.randomAlphanumeric(passwordLength);
	}

	public static SecretKey generateSecretKey(final String encryption) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(encryption);
		SecureRandom random = new SecureRandom();
		keyGen.init(random);
		return keyGen.generateKey();
	}
}
