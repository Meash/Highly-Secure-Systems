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
		Object obj = null;
		try {
			obj = serializer.deserialize(textContent);

		} catch (IOException ignored) {
			return; // could not be deserialized -> not an object message -> ignore
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!(obj instanceof EncryptedMessage)) {
			return;
		}
		Message msg = null;
		try {
			try {
				msg = messageEncrypter.decrypt((EncryptedMessage) obj, new RSA(privateKey, null));
			} catch (ClassNotFoundException e) {
				sendSMS(phone, new ProtocolInvalidationMessage("Message could not be decrypted"));
				return;
			} catch (CryptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!(msg instanceof CommunicationRequestMessage))
				sendSMS(phone, new ProtocolInvalidationMessage(
						"Expected " + CommunicationRequestMessage.class.getSimpleName() + ", got " +
								msg.getClass().getName()));
			final CommunicationDisplay display = app.accept(phone);
			if (display == null) // denied
				return;
			sendSMS(phone, new CommunicationConfirmationMessage(((CommunicationRequestMessage) msg).nonce));
		} catch(IOException e) {
			app.displayError("Could not send SMS: " + e.getMessage());
			return;
		}
		ClientCommunication communication = new PassiveClientCommunication(phone, privateKey, ((CommunicationRequestMessage) msg).sessionKey);
		smsReceiver.addListener(phone, communication);
	}

	private void sendSMS(final String phone, final Message msg) throws IOException {
		smsSender.send(phone, serializer.serialize(msg));
	}
}
