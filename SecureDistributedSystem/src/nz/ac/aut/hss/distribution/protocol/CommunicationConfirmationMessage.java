package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class CommunicationConfirmationMessage extends Message {
	public static final String IDENTIFIER = "communication_confirmation";
	public final String nonce;

	public CommunicationConfirmationMessage(final String nonce, final Encryption... encryptions) {
		super(IDENTIFIER, encryptions);
		this.nonce = nonce;
	}
}
