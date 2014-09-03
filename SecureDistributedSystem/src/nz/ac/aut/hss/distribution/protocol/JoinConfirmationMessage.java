package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinConfirmationMessage extends Message {
	public static final String IDENTIFIER = "join_confirmation";
	public final String nonce;

	public JoinConfirmationMessage(final String nonce, final Encryption... encryptions) {
		super(IDENTIFIER, encryptions);
		this.nonce = nonce;
	}
}
