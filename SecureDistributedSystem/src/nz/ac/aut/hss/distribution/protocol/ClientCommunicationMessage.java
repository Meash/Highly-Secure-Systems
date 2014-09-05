package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class ClientCommunicationMessage extends SimpleTextMessage {
	public static final String IDENTIFIER = "client_communication";

	public ClientCommunicationMessage(final String content, final Encryption... encryptions) {
		super(IDENTIFIER, content, encryptions);
	}
}
