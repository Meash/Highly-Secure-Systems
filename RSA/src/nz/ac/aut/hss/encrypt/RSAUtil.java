package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAUtil {
	private static final Random ran = new SecureRandom();

	public static BigInteger probablePrime(int bits) {
		return MillerRabin.randomBig(BigInteger.valueOf(bits), ran);
//		return BigInteger.probablePrime(bits, ran);
	}

	public static EuclidResult extendedEuclid(BigInteger m, BigInteger n) {
		final BigInteger ZERO = new BigInteger("0");
		final BigInteger ONE = new BigInteger("1");
		if (n.equals(ZERO)) {
			return new EuclidResult(m, ONE, ZERO);
		}
		EuclidResult resultPrime = extendedEuclid(n, m.mod(n));
		BigInteger q = m.divide(n);
		return new EuclidResult(resultPrime.d, resultPrime.t, resultPrime.s.subtract(q.multiply(resultPrime.t)));
	}

	public static RSAKeyPair generateKeyPair(int bits) {
		if (bits < 512) {
			bits = 512;
		}

		BigInteger p = probablePrime(bits);
		BigInteger q = probablePrime(bits);

		BigInteger n = p.multiply(q);
		BigInteger totient = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
		int e = 65537;
		BigInteger d = extendedEuclid(BigInteger.valueOf(e), totient).d;
		while (d.compareTo(BigInteger.valueOf(0)) == -1) {
			d = d.add(n);
		}
		RSAKey privateKey = new RSAKey(n, d);
		RSAKey publicKey = new RSAKey(n, BigInteger.valueOf(e));
		return new RSAKeyPair(privateKey, publicKey);
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
		BigInteger result = BigInteger.ONE;
		final BigInteger TWO = new BigInteger("2");

		while (b.compareTo(BigInteger.ZERO) == 1) {
			if (b.mod(TWO).compareTo(BigInteger.ONE) == 0)
				result = (result.multiply(a)).mod(n);
			b = b.divide(TWO);
			a = (a.multiply(a)).mod(n);
		}

		return result;
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
