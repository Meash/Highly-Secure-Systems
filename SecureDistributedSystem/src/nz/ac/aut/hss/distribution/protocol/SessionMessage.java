package nz.ac.aut.hss.distribution.protocol;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SessionMessage extends Message {
	/** AES secret key */
	public final SecretKey sessionKey;
	public final String nonce;

	public SessionMessage(final SecretKey sessionKey, final String nonce) {
		super();
		this.sessionKey = sessionKey;
		this.nonce = nonce;
	}
}
