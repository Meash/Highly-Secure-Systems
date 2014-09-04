package nz.ac.aut.hss.client.communication;

import java.security.KeyPair;
import java.security.KeyStoreException;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface KeyStore {
	public KeyPair loadOrCreateAndSaveKeyPair() throws KeyStoreException;
}
