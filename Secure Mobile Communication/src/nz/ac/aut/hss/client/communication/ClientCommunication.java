package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.protocol.Message;

import java.security.PrivateKey;

/**
 * One client communication serves as the intermediary between this app and one other client.
 * For multiple clients, multiple client communications are needed.
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public abstract class ClientCommunication implements SMSListener {
	protected final ClientMessageEncrypter messageEncrypter;

	/**
	 * @param ownPrivateKey this client's private key
	 */
	public ClientCommunication(final PrivateKey ownPrivateKey) {
		messageEncrypter = new ClientMessageEncrypter(ownPrivateKey);
	}


	public abstract void sendMessage(final String message, final boolean confidential, final boolean authenticate);

	public boolean isMessageConfidential(final Message message) {
		return true;
	}

	public boolean isMessageAuthentic(final Message message) {
		return true;
	}
}
