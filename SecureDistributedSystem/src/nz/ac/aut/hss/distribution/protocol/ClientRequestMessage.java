package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientRequestMessage extends Message {
	public static final String IDENTIFIER = "client_request_pubkey";
	public final String phone;

	/**
	 * @param phone     the phone number of the client
	 */
	public ClientRequestMessage(final String phone, final Encryption... encryptions) {
		super(IDENTIFIER);
		if (phone == null)
			throw new IllegalArgumentException("Phone must not be null");
		this.phone = phone;
	}
}
