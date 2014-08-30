package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SessionMessage extends Message {
	/** AES secret key */
	public final SecretKey sessionKey;
	public final String nonce;

	public SessionMessage(final SecretKey sessionKey, final String nonce, final Encryption... encryptions) {
		super(encryptions);
		this.sessionKey = sessionKey;
		this.nonce = nonce;
	}
}
