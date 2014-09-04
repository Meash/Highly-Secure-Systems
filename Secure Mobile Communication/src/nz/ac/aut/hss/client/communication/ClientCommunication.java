package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.protocol.Message;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientCommunication {
	private final ClientMessageEncrypter messageEncrypter;

	/**
	 * @param ownPrivateKey this client's private key
	 * @throws InstantiationException if the communication could not be established
	 */
	public ClientCommunication(final PrivateKey ownPrivateKey) throws InstantiationException {
		messageEncrypter = new ClientMessageEncrypter(ownPrivateKey);
	}

	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 */
	public void initializeNewCommunication(final String partnerPhoneNumber) {
		// TODO: init communication
		final SecretKey sessionKey = null;
		messageEncrypter.putSessionKey(partnerPhoneNumber, sessionKey);
	}

	public void sendMessage(final String message, final boolean confidential, final boolean authenticate) {

	}

	public boolean isMessageConfidential(final Message message) {
		return true;
	}

	public boolean isMessageAuthentic(final Message message) {
		return true;
	}
}
