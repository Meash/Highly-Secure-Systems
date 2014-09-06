package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class EncryptedClientCommunicationMessage extends SimpleTextMessage{
	public static final String IDENTIFIER = "client_communication_encrypted";

	public EncryptedClientCommunicationMessage(final String encryptedContent) {
		super(IDENTIFIER, encryptedContent);
	}
}
