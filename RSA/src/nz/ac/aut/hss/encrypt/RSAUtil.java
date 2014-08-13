package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAUtil {
	public static boolean probablyPrime(BigInteger n) {
		return true; // TODO
	}

	public static EuclidResult extendedEuclid(BigInteger m, BigInteger n) {
		BigInteger d = m.gcd(n);
		BigInteger zero = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		if(n.equals(zero)){
			return new EuclidResult(m, one, zero);
		}
		EuclidResult resultPrime = extendedEuclid(n, m.mod(n));
		BigInteger q = m.divide(n);
		return new EuclidResult(resultPrime.d, resultPrime.t, resultPrime.s.subtract(q.multiply(resultPrime.t)));
	}

	public static RSAKeyPair generateKeyPair() {
		return null; // TODO
	}

	private static class EuclidResult {
		public final BigInteger d, s, t;

		public EuclidResult(BigInteger d, BigInteger s, BigInteger t) {
			this.d = d;
			this.s = s;
			this.t = t;
		}
	}

	public static BigInteger modularExponentiation(BigInteger a, BigInteger b, BigInteger n) {
		return null; // TODO
	}

	public static BigInteger toNumber(String str) {
		byte[] bytes = str.getBytes();
		BigInteger result = BigInteger.valueOf(0);
		final int SIGN_MASK = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			int val = bytes[i] & SIGN_MASK; // retain signed bit
			int shift = (bytes.length - 1 - i) * 8; // big endian
			BigInteger interim = BigInteger.valueOf(val);
			interim = interim.shiftLeft(shift);
			result = result.or(interim);
		}
		return result;
	}

	public static String toString(BigInteger n) {
		final List<Byte> bytes = new LinkedList<>();
		while (n.bitLength() > 0) {
			bytes.add(0, n.byteValue()); // insert front
			n = n.shiftRight(8);
		}
		return new String(toPrimitive(bytes.toArray(new Byte[bytes.size()])));
	}

	public static byte[] toPrimitive(Byte[] arr) {
		byte[] result = new byte[arr.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = arr[i];
		}
		return result;
	}
}
