package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.protocol.Message;

import javax.crypto.SecretKey;

import java.security.PrivateKey;

/**
 * Client communication that initializes the communication with another client
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ActiveClientCommunication extends ClientCommunication {
	private final String partnerPhoneNumber;

	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 * @param ownPrivateKey      this client's private key
	 */
	public ActiveClientCommunication(final String partnerPhoneNumber, final PrivateKey ownPrivateKey,
									 final SMSReceiver smsReceiver) {
		super(ownPrivateKey);
		if (partnerPhoneNumber == null || partnerPhoneNumber.length() == 0)
			throw new IllegalArgumentException("partnerPhoneNumber is null or empty");
		this.partnerPhoneNumber = partnerPhoneNumber;
		if(smsReceiver == null)
			throw new IllegalArgumentException("smsReceiver must not be null");
		smsReceiver.addListener(partnerPhoneNumber, this);

		initializeCommunication();
	}

	private void initializeCommunication() {
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

	@Override
	public void receive(String phone, String textContent) {
		// TODO Auto-generated method stub
		
	}
}
