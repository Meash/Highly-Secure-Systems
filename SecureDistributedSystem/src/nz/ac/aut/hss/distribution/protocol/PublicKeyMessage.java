package nz.ac.aut.hss.distribution.protocol;

import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PublicKeyMessage extends Message {
	public final String phone;
	public final ECPublicKey publicKey;

	/**
	 * @param phone     the phone number of the client
	 * @param publicKey the public key of the client or null for a request
	 */
	public PublicKeyMessage(final String phone, final ECPublicKey publicKey) {
		if (phone == null)
			throw new IllegalArgumentException("Phone must not be null");
		this.phone = phone;
		this.publicKey = publicKey;
	}
}
