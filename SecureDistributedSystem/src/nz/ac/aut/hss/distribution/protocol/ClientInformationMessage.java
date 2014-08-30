package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientInformationMessage extends Message {
	public final String telephoneNumber, nonce;
	public final ECPublicKey publicKey;

	public ClientInformationMessage(final String telephoneNumber, final ECPublicKey publicKey, final String nonce, final Encryption... encryptions) {
		super(encryptions);
		this.telephoneNumber = telephoneNumber;
		this.publicKey = publicKey;
		this.nonce = nonce;
	}
}
