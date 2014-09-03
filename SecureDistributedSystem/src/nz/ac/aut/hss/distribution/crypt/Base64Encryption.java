package nz.ac.aut.hss.distribution.crypt;

import nz.ac.aut.hss.distribution.util.Base64Coder;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class Base64Encryption implements Encryption {
	@Override
	public String encrypt(final String str) {
		return Base64Coder.encodeString(str);
	}

	@Override
	public String decrypt(final String cipher) throws CryptException {
		return Base64Coder.decodeString(cipher);
	}
}
