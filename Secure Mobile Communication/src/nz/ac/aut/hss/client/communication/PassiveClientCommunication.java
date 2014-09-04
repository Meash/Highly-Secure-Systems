package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.protocol.Message;

import java.security.PrivateKey;

/**
 * One client communication serves as the intermediary between this app and one other client.
 * For multiple clients, multiple client communications are needed.
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class PassiveClientCommunication extends ClientCommunication {
	private final String partnerPhoneNumber;

	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 * @param ownPrivateKey      this client's private key
	 */
	public PassiveClientCommunication(final String partnerPhoneNumber, final PrivateKey ownPrivateKey,
									  final SMSReceiver smsReceiver) {
		super(ownPrivateKey);
		if (partnerPhoneNumber == null || partnerPhoneNumber.length() == 0)
			throw new IllegalArgumentException("partnerPhoneNumber is null or empty");
		this.partnerPhoneNumber = partnerPhoneNumber;
	}

	public void sendMessage(final String message, final boolean confidential, final boolean authenticate) {

	}

	public boolean isMessageConfidential(final Message message) {
		return true;
	}

	public boolean isMessageAuthentic(final Message message) {
		return true;
	}

	@Override
	public void receive(final String textContent) {

	}
}
