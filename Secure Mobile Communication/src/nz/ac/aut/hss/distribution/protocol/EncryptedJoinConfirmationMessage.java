package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class EncryptedJoinConfirmationMessage extends Message {
	public static final String IDENTIFIER = "encrypted_join_confirmation";
	public final String encryptedNonce;

	public EncryptedJoinConfirmationMessage(final String encryptedNonce) {
		super(IDENTIFIER);
		this.encryptedNonce = encryptedNonce;
	}
}
