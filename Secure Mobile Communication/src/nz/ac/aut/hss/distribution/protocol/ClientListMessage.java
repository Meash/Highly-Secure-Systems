package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.PublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientListMessage extends Message {
	public static final String IDENTIFIER = "client_list";

	/** the client list (null for a request) */
	public final Map<String, PublicKey> phonePublicKey;

	public ClientListMessage(final Map<String, PublicKey> phonePublicKey, final Encryption... encryptions) {
		super(IDENTIFIER);
		this.phonePublicKey = phonePublicKey;
	}
}
