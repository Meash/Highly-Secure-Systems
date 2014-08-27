package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class CommunicationConfirmationMessage {
	public final String nonce;

	public CommunicationConfirmationMessage(final String nonce) {
		this.nonce = nonce;
	}
}
