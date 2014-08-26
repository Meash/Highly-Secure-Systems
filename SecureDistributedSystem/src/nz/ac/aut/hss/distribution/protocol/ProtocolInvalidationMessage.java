package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ProtocolInvalidationMessage extends Message {
	public final String message;

	public ProtocolInvalidationMessage(final String message, final EncryptionMode... encryptions) {
		super(encryptions);
		this.message = message;
	}
}
