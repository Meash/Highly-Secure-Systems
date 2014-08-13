package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSA {
	/**
	 * Encryption and Decryption of RSA.
	 * For Encryption, the exponent value of the key will be e and for Decryption, it will be d.
	 * The modulo value of the key represents n.
	 * @param text the plain- or cipher-text
	 * @param key the key to convert with (either public or private)
	 * @return the cipher- or plain-text depending on the arguments
	 */
	public String convert(String text, RSAKey key) {
		BigInteger P = RSAUtil.toNumber(text);
		BigInteger C = RSAUtil.modularExponentiation(P, key.exponent, key.modulo);
		return RSAUtil.toString(C);
	}

	public static class RSAKey {
		public final BigInteger modulo, exponent;

		public RSAKey(final BigInteger modulo, final BigInteger exponent) {
			this.modulo = modulo;
			this.exponent = exponent;
		}
	}
}
