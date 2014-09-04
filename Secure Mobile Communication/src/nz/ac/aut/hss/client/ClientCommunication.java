package nz.ac.aut.hss.client;

import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.protocol.Message;

import javax.crypto.SecretKey;
import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientCommunication {
	private final ClientMessageEncrypter messageEncrypter;

	/**
	 * @param ownPublicKey this client's public key
	 * @throws InstantiationException if the communication could not be established
	 */
	public ClientCommunication(final ECPublicKey ownPublicKey) throws InstantiationException {
		messageEncrypter = new ClientMessageEncrypter(ownPublicKey);
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
