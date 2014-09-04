package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.PublicKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientInformationMessage extends Message {
	public static final String IDENTIFIER = "join_request_client_info";
	public final String telephoneNumber, nonce;
	public final PublicKey publicKey;

	public ClientInformationMessage(final String telephoneNumber, final PublicKey publicKey, final String nonce, final Encryption... encryptions) {
		super(IDENTIFIER, encryptions);
		this.telephoneNumber = telephoneNumber;
		this.publicKey = publicKey;
		this.nonce = nonce;
	}
}
