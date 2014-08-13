package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAUtil {
	public static boolean probablyPrime(BigInteger n) {
		return true; // TODO
	}

	public static EudlidResult extendedEuclid(BigInteger m, BigInteger n) {
		return null; // TODO
	}

	private static class EudlidResult {
		public final BigInteger d, s, t;

		public EudlidResult(BigInteger d, BigInteger s, BigInteger t) {
			this.d = d;
			this.s = s;
			this.t = t;
		}
	}

	public static BigInteger modularExponentiation(BigInteger a, BigInteger b, BigInteger n) {
		return null; // TODO
	}

	public static BigInteger toNumber(String str) {
		return new BigInteger(str.getBytes());
	}

	public static String toString(BigInteger n) {
		new String(n.toByteArray());
	}
}
