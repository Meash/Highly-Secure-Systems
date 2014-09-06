package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.util.Arrays;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class KeyUtil {
	private final String algorithm;

	public KeyUtil(final String algorithm) {
		this.algorithm = algorithm;
	}

	public String toString(Key key) {
		byte[] b = key.getEncoded();
		byte[] b2 = new byte[b.length + 1];
		b2[0] = 1;
		System.arraycopy(b, 0, b2, 1, b.length);
		return new BigInteger(b2).toString(36);
	}

	public SecretKey toKey(String string) {
		byte[] b2 = new BigInteger(string, 36).toByteArray();
		byte[] b = Arrays.copyOfRange(b2, 1, b2.length);
		return new SecretKeySpec(b, algorithm);
	}
}
