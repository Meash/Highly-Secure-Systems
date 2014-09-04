package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.CommunicationRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

/**
 * Client communication that initializes the communication with another client
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ActiveClientCommunication extends ClientCommunication {
	private final String partnerPhoneNumber;
	private final ServerCommunication serverCommunication;
	private final ObjectSerializer serializer;

	/**
	 * @param partnerPhoneNumber  the phone number of the partner we are connecting to
	 * @param ownPrivateKey       this client's private key
	 * @param serverCommunication the communicator with the trusted key server
	 */
	public ActiveClientCommunication(final String partnerPhoneNumber, final PrivateKey ownPrivateKey,
									 final ServerCommunication serverCommunication) {
		super(ownPrivateKey);
		if (partnerPhoneNumber == null || partnerPhoneNumber.length() == 0)
			throw new IllegalArgumentException("partnerPhoneNumber is null or empty");
		this.partnerPhoneNumber = partnerPhoneNumber;
		if (serverCommunication == null) throw new IllegalArgumentException("serverCommunication is null");
		this.serverCommunication = serverCommunication;
		serializer = new ObjectSerializer();
	}

	public void initializeCommunication() throws CommunicationException, ClientDoesNotExistException {
		/* ask server about partner's public key */
		partnerPublicKey = serverCommunication.requestClient(partnerPhoneNumber);
		/* request communication with client */
		try {
			sessionKey = AES.createKey(128);
		} catch (NoSuchProviderException | NoSuchAlgorithmException e) {
			throw new CommunicationException("Could not create session key", e);
		}
		messageEncrypter.setSessionKey(sessionKey);
		final String nonce = RandomStringUtils.randomAlphanumeric(Message.NONCE_LENGTH);
		Message msg = new CommunicationRequestMessage(nonce, sessionKey, this.partnerPublicKey, new RSA(null, partnerPublicKey));
		try {
			sendMessage(serializer.serialize(msg), false, false);
		} catch (IOException e) {
			throw new CommunicationException("Could not serialize message", e);
		}
	}

	@Override
	public void receive(final String phone, final String textContent) {

	}
}
