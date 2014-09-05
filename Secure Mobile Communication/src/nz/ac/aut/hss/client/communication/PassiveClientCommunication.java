package nz.ac.aut.hss.client.communication;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

/**
 * One client communication serves as the intermediary between this app and one other client.
 * For multiple clients, multiple client communications are needed.
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class PassiveClientCommunication extends ClientCommunication {
	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 * @param ownPrivateKey      this client's private key
	 * @param sessionKey         the secret key for the communication
	 */
	public PassiveClientCommunication(final String partnerPhoneNumber, final MobileApp app,
									  final CommunicationDisplay display, final SMSSender smsSender,
									  final PrivateKey ownPrivateKey, final SecretKey sessionKey)
			throws CommunicationException {
		super(partnerPhoneNumber, app, display, smsSender, ownPrivateKey);
		if (sessionKey == null) throw new IllegalArgumentException("sessionKey is null");
		setSessionKey(sessionKey);
	}

}
