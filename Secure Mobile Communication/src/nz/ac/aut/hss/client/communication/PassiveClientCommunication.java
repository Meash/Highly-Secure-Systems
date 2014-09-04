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
	private final SecretKey secretKey;
	private final String partnerPhoneNumber;

	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 * @param ownPrivateKey      this client's private key
	 * @param secretKey the secret key for the communication
	 */
	public PassiveClientCommunication(final String partnerPhoneNumber, final PrivateKey ownPrivateKey,
									  final SecretKey secretKey) {
		super(ownPrivateKey);
		if (partnerPhoneNumber == null || partnerPhoneNumber.length() == 0) {
			throw new IllegalArgumentException("partnerPhoneNumber is null or empty");
		}
		this.partnerPhoneNumber = partnerPhoneNumber;
		if(secretKey == null) throw new IllegalArgumentException("secretKey is null");
		this.secretKey = secretKey;
	}

	public void sendMessage(final String message, final boolean confidential, final boolean authenticate) {

	}

	@Override
	public void receive(final String phone, final String textContent) {

	}
}
