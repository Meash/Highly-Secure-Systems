package nz.ac.aut.hss.distribution.crypt;

import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.IOException;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class MessageEncrypter {
	private final ObjectSerializer serializer;

	public MessageEncrypter() {
		serializer = new ObjectSerializer();
	}

	public EncryptedMessage applyEncryptions(Message msg) throws CryptException {
		String result;
		try {
			result = serializer.serialize(msg);
		} catch (IOException e) {
			throw new CryptException("Could not serialize message", e);
		}
		for (int i = 0; i < msg.encryptions.length; i++) {
			result = msg.encryptions[i].encrypt(result);
		}
		return new EncryptedMessage(result, msg.encryptions);
	}

	public Message decrypt(EncryptedMessage msg) throws CryptException, IOException, ClassNotFoundException {
		String result = msg.content;
		for (int i = msg.encryptions.length - 1; i >= 0; i--) {
			result = msg.encryptions[i].decrypt(result);
		}
		Object obj = serializer.deserialize(result);
		if(! (obj instanceof Message))
			throw new IllegalArgumentException("Decrypted object is not a message");
		return (Message) obj;
	}
}
