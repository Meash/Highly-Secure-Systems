package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientInformationMessage extends Message {
	public static final String IDENTIFIER = "join_request_client_info";
	public final String telephoneNumber, nonce;
	public final String publicKey;

	public ClientInformationMessage(final String telephoneNumber, final String publicKey, final String nonce, final Encryption... encryptions) {
		super(IDENTIFIER);
		if(telephoneNumber == null || telephoneNumber.isEmpty())
			throw new IllegalArgumentException("telephoneNumber is null or empty");
		this.telephoneNumber = telephoneNumber;
		if(publicKey == null)
			throw new IllegalArgumentException("publicKey is null");
		this.publicKey = publicKey;
		if(nonce == null || nonce.isEmpty())
			throw new IllegalArgumentException("nonce is null or empty");
		this.nonce = nonce;
	}
}
