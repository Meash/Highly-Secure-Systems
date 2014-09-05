package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.SimpleTextMessage;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

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

	private final String partnerPhoneNumber;
	private final MobileApp app;
	private final CommunicationDisplay display;
	protected final ClientMessageEncrypter messageEncrypter;
	private final MessageAuthenticator authenticator;
	protected final ObjectSerializer serializer;
	protected final PrivateKey privateKey;
	private Encryption encryption;
	protected PublicKey partnerPublicKey;
	private final SMSSender smsSender;

	public ClientCommunication(final String partnerPhoneNumber, final MobileApp app, final CommunicationDisplay display,
							   final SMSSender smsSender, final PrivateKey ownPrivateKey) {
		if (partnerPhoneNumber == null || partnerPhoneNumber.length() == 0)
			throw new IllegalArgumentException("partnerPhoneNumber is null or empty");
		this.partnerPhoneNumber = partnerPhoneNumber;
		if (app == null) throw new IllegalArgumentException("app is null");
		this.app = app;
		if (display == null) throw new IllegalArgumentException("display is null");
		this.display = display;
		messageEncrypter = new ClientMessageEncrypter(ownPrivateKey);
		if (smsSender == null) throw new IllegalArgumentException("smsSender is null");
		this.smsSender = smsSender;
		if (ownPrivateKey == null) throw new IllegalArgumentException("ownPrivateKey is null");
		privateKey = ownPrivateKey;
		try {
			authenticator = new MessageAuthenticator();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not create authenticator", e);
		}
		serializer = new ObjectSerializer();
	}

	public void sendMessage(final String message, final boolean confidential, final boolean authenticate)
			throws CommunicationException {
		Message msg = new SimpleTextMessage(ClientCommunication.MESSAGE_IDENTIFIER, message, encryptions);
		try {
			msg = messageEncrypter.applyEncryptions(msg);
		} catch (CryptException e) {
			throw new CommunicationException("Could not encrypt message", e);
		}
		if (authenticate) {
			try {
				msg.authentication = authenticator.hash(msg, privateKey);
			} catch (CryptException e) {
				throw new CommunicationException("Could not create authentication", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final String content;
		try {
			content = serializer.serialize(msg);
		} catch (IOException e) {
			throw new CommunicationException("Could not serialize message", e);
		}
		smsSender.send(partnerPhoneNumber, content);
	}

	@Override
	public void receive(final String phone, final String textContent) {
		final Object obj;
		try {
			obj = serializer.deserialize(textContent);
		} catch (ClassNotFoundException | IOException e) {
			display.addReceivedMessage(textContent, false, false);
			return;
		}
		if (!(obj instanceof Message)) {
			display.addReceivedMessage(textContent, false, false);
			return;
		}
		Message msg = (Message) obj;

		/* authenticity */
		boolean authentic;
		try {
			authentic = isMessageAuthentic(msg);
		} catch (IOException | CryptException e) {
			authentic = false;
		}

		/* encryption */
		boolean confidential = isMessageConfidential(msg);
		if (confidential) {
			try {
				msg = messageEncrypter.decrypt((EncryptedMessage) msg, createEncryptions());
				confidential = true;
			} catch (CryptException | ClassNotFoundException | IOException e) {
				confidential = false;
			}
		}

		if (!(msg instanceof SimpleTextMessage)) {
			app.displayError(
					"Protocol invalidation: Expected message of class " + SimpleTextMessage.class.getSimpleName() +
							", got " + msg.getClass().getName());
			return;
		}

		display.addReceivedMessage(((SimpleTextMessage) msg).content, confidential, authentic);
	}

	public boolean isMessageConfidential(final Message message) {
		return message instanceof EncryptedMessage;
	}

	public boolean isMessageAuthentic(final Message message) throws IOException, CryptException {
		if (partnerPublicKey == null) throw new IllegalStateException("partnerPublicKey has not been set");
		return authenticator.verify(message, partnerPublicKey);
	}

	private Encryption[] createEncryptions() {
		if (encryption == null) throw new IllegalStateException("encryption has not been set");
		return new Encryption[]{encryption};
	}

	protected void setSessionKey(final SecretKey key) throws CommunicationException {
		try {
			encryption = new AES(key);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new CommunicationException("Could not initialize encryption", e);
		}
	}
}
