package nz.ac.aut.hss.client;

import nz.ac.aut.hss.client.communication.KeyStore;
import nz.ac.aut.hss.distribution.crypt.RSA;

import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public class KeyStoreDummy implements KeyStore {
	@Override
	public KeyPair loadOrCreateAndSaveKeyPair() throws KeyStoreException {
		try {
			return RSA.createKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new KeyStoreException(e);
		}
	}
}
