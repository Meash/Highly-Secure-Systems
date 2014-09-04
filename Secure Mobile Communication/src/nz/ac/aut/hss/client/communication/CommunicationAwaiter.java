package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.MessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.IOException;
import java.security.PrivateKey;

/**
 * @author Martin Schrimpf
 * @created 05.09.2014
 */
public class CommunicationAwaiter implements SMSListener {
	private final MobileApp app;
	private final ObjectSerializer serializer;
	private final MessageEncrypter messageEncrypter;
	private final PrivateKey privateKey;
	private final SMSReceiver smsReceiver;
	private final SMSSender smsSender;

	public CommunicationAwaiter(final MobileApp app, final PrivateKey privateKey,
								final SMSReceiver smsReceiver, final SMSSender smsSender) {
		if (app == null) throw new IllegalArgumentException("app must not be null");
		this.app = app;
		if (privateKey == null) throw new IllegalArgumentException("privateKey must not be null");
		this.privateKey = privateKey;
		if (smsReceiver == null) throw new IllegalArgumentException("smsReceiver must not be null");
		this.smsReceiver = smsReceiver;
		if (smsSender == null) throw new IllegalArgumentException("smsSender must not be null");
		this.smsSender = smsSender;
		serializer = new ObjectSerializer();
		messageEncrypter = new MessageEncrypter();
	}

	@Override
	public void receive(final String phone, final String textContent) {
		final Object obj;
		try {
			obj = serializer.deserialize(textContent);

		} catch (ClassNotFoundException | IOException ignored) {
			return; // could not be deserialized -> not an object message -> ignore
		}
		if (!(obj instanceof EncryptedMessage)) {
			return;
		}
		final Message msg;
		CommunicationDisplay display;
		try {
			try {
				msg = messageEncrypter.decrypt((EncryptedMessage) obj, new RSA(privateKey, null));
			} catch (CryptException | IOException | ClassNotFoundException e) {
				sendSMS(phone, new ProtocolInvalidationMessage("Message could not be decrypted"));
				return;
			}
			if (!(msg instanceof CommunicationRequestMessage))
				sendSMS(phone, new ProtocolInvalidationMessage(
						"Expected " + CommunicationRequestMessage.class.getSimpleName() + ", got " +
								msg.getClass().getName()));
			display = app.accept(phone);
			if (display == null) // denied
				return;
			sendSMS(phone, new CommunicationConfirmationMessage(((CommunicationRequestMessage) msg).nonce));
		} catch (IOException e) {
			app.displayError("Could not send SMS: " + e.getMessage());
			return;
		}
		ClientCommunication communication;
		try {
			communication = new PassiveClientCommunication(phone, app, display, smsSender, privateKey,
					((CommunicationRequestMessage) msg).sessionKey);
		} catch (CommunicationException e) {
			app.displayError("Could not create client communication: " + e.getMessage());
			return;
		}
		smsReceiver.addListener(phone, communication);
	}

	private void sendSMS(final String phone, final Message msg) throws IOException {
		smsSender.send(phone, serializer.serialize(msg));
	}
}
