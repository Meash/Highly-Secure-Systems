package nz.ac.aut.hss.distribution.protocol;

import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientInformationMessage extends Message {
	public final String telephoneNumber, nonce;
	public final ECPublicKey publicKey;

	public ClientInformationMessage(final String telephoneNumber, final ECPublicKey publicKey, final String nonce, final EncryptionMode... encryptions) {
		super(encryptions);
		this.telephoneNumber = telephoneNumber;
		this.publicKey = publicKey;
		this.nonce = nonce;
	}
}
