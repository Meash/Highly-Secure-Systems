package nz.ac.aut.hss.distribution.protocol;

import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientListMessage extends Message {
	/** the client list (null for a request) */
	public final Map<String, String> phonePublicKey;

	public ClientListMessage(final Map<String, String> phonePublicKey) {
		this.phonePublicKey = phonePublicKey;
	}
}
