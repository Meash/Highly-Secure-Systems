package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class CommunicationRequestMessage extends Message {
	public static final String IDENTIFIER = "client_list_request";
	public final String nonce;
	public final SecretKey sessionKey;

	public CommunicationRequestMessage(final String nonce, final SecretKey sessionKey,
									   final Encryption... encryptions) {
		super(IDENTIFIER, encryptions);
		this.nonce = nonce;
		this.sessionKey = sessionKey;
	}
}
