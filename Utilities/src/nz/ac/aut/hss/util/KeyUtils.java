package nz.ac.aut.hss.util;

import java.security.SecureRandom;

/**
 * @author Martin Schrimpf
 * @created 05.08.2014
 */
public class KeyUtils {
	public static String randomKey(int length, char[] alphabet) {
		final StringBuilder builder = new StringBuilder();
		final SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt() % alphabet.length;
			if(index < 0)
				index += alphabet.length;
			builder.append(alphabet[index]);
		}
		return builder.toString();
	}
}
