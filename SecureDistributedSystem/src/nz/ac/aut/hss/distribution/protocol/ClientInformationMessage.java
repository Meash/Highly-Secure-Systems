package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientInformationMessage extends Message {
	public final String telephoneNumber, publickey, nonce;

	public ClientInformationMessage(final String telephoneNumber, final String publickey, final String nonce) {
		this.telephoneNumber = telephoneNumber;
		this.publickey = publickey;
		this.nonce = nonce;
	}
}
