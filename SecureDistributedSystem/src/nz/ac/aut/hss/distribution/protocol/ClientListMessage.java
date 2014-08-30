package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.interfaces.ECPublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientListMessage extends Message {
	/** the client list (null for a request) */
	public final Map<String, ECPublicKey> phonePublicKey;

	public ClientListMessage(final Map<String, ECPublicKey> phonePublicKey, final Encryption... encryptions) {
		super(encryptions);
		this.phonePublicKey = phonePublicKey;
	}
}
