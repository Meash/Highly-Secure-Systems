package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.SimpleTextMessage;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * One client communication serves as the intermediary between this app and one other client.
 * For multiple clients, multiple client communications are needed.
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public abstract class ClientCommunication implements SMSListener {
	public static final String MESSAGE_IDENTIFIER = "client_communication";
	protected final ClientMessageEncrypter messageEncrypter;
	protected final PrivateKey privateKey;
	protected SecretKey sessionKey;
	private final MessageAuthenticator authenticator;
	protected PublicKey partnerPublicKey;

	/**
	 * @param ownPrivateKey this client's private key
	 */
	public ClientCommunication(final PrivateKey ownPrivateKey) {
		messageEncrypter = new ClientMessageEncrypter(ownPrivateKey);
		privateKey = ownPrivateKey;
		try {
			authenticator = new MessageAuthenticator();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not create authenticator", e);
		}
	}


	public void sendMessage(final String message, final boolean confidential, final boolean authenticate)
			throws CommunicationException {
		Encryption[] encryptions;
		try {
			if (confidential) {
				if (sessionKey == null) throw new IllegalStateException("sessionKey has not been set");
				encryptions = new Encryption[]{new AES(sessionKey)};
			} else encryptions = new Encryption[0];
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new CommunicationException("Could not initialize encryption", e);
		}
		Message msg = new SimpleTextMessage(ClientCommunication.MESSAGE_IDENTIFIER, message, encryptions);
		try {
			msg = messageEncrypter.applyEncryptions(msg);
		} catch (CryptException e) {
			throw new CommunicationException("Could not encrypt message", e);
		}
		if (authenticate) {
			try {
				msg.authentication = authenticator.hash(msg, privateKey);
			} catch (IOException | CryptException e) {
				throw new CommunicationException("Could not create authentication", e);
			}
		}
	}

	public boolean isMessageConfidential(final Message message) throws IOException, CryptException {
		if(partnerPublicKey == null) throw new IllegalStateException("partnerPublicKey has not been set");
		return authenticator.verify(message, partnerPublicKey);
	}

	public boolean isMessageAuthentic(final Message message) {
		return true;
	}
}
