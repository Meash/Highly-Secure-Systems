package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinConfirmationMessage extends Message {
	public static final String IDENTIFIER = "join_confirmation";
	public final String nonce;

	public JoinConfirmationMessage(final String nonce) {
		super(IDENTIFIER);
		this.nonce = nonce;
	}
}
