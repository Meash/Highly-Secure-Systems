package nz.ac.aut.hss.distribution.crypt;

import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class ECCEncryption extends Encryption {
	private final ECPublicKey publicKey;

	public ECCEncryption(final ECPublicKey publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String encrypt(final String str) throws CryptException {
		return null;
	}

	@Override
	public String decrypt(final String cipher) throws CryptException {
		return null;
	}
}
