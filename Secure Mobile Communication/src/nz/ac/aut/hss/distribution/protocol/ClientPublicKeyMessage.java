package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.PublicKey;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientPublicKeyMessage extends Message {
	public static final String IDENTIFIER = "client_public_key";
	public final String phone;
	public final PublicKey publicKey;

	/**
	 * @param phone     the phone number of the client
	 * @param publicKey the public key of the client
	 */
	public ClientPublicKeyMessage(final String phone, final PublicKey publicKey, final Encryption... encryptions) {
		super(IDENTIFIER);
		if (phone == null)
			throw new IllegalArgumentException("Phone must not be null");
		this.phone = phone;
		if(publicKey == null)
			throw new IllegalArgumentException("public key must not be null");
		this.publicKey = publicKey;
	}
}
