package nz.ac.aut.hss.encrypt;

import java.math.BigInteger;

/**
* @author Martin Schrimpf
* @created 13.08.2014
*/
public class RSAKey {
	public final BigInteger modulo, exponent;

	public RSAKey(final BigInteger modulo, final BigInteger exponent) {
		this.modulo = modulo;
		this.exponent = exponent;
	}
}
