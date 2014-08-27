package nz.ac.aut.hss.distribution.protocol;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class CommunicationRequestMessage {
	public final String nonce;
	public final SecretKey sessionKey;

	public CommunicationRequestMessage(final String nonce, final SecretKey sessionKey) {
		this.nonce = nonce;
		this.sessionKey = sessionKey;
	}
}
