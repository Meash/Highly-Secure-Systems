package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PublicKeyMessage extends Message {
	public static final String IDENTIFIER = "client_public_key";
	public final String phone;
	public final ECPublicKey publicKey;

	/**
	 * @param phone     the phone number of the client
	 * @param publicKey the public key of the client or null for a request
	 */
	public PublicKeyMessage(final String phone, final ECPublicKey publicKey, final Encryption... encryptions) {
		super(IDENTIFIER, encryptions);
		if (phone == null)
			throw new IllegalArgumentException("Phone must not be null");
		this.phone = phone;
		this.publicKey = publicKey;
	}
}
