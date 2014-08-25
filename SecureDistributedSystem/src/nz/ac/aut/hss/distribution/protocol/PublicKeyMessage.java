package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PublicKeyMessage extends Message {
	public final String phone;
	public final String publicKey;

	public PublicKeyMessage(final String phone, final String publicKey) {
		if (phone != null && publicKey != null)
			throw new IllegalArgumentException("Either phone or public key should be null");
		if (phone == null && publicKey == null)
			throw new IllegalArgumentException("Both phone and public key are null");
		this.phone = phone;
		this.publicKey = publicKey;
	}
}
