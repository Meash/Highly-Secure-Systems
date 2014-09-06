package nz.ac.aut.hss.distribution.crypt;

import nz.ac.aut.hss.distribution.protocol.ClientCommunicationMessage;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;

import java.io.IOException;
import java.security.PrivateKey;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientMessageEncrypter extends MessageEncrypter {
	private final PrivateKey privateKey;

	public ClientMessageEncrypter(final PrivateKey privateKey) {
		if (privateKey == null)
			throw new IllegalArgumentException("private key is null");
		this.privateKey = privateKey;
	}

	public Message decrypt(final Message input) throws IOException, CryptException, ClassNotFoundException {
		if (!(input instanceof EncryptedMessage))
			return input;

		final Encryption[] encryptions = new Encryption[]{new RSA(privateKey, null)};
		if (!input.identifier.equals(ClientCommunicationMessage.IDENTIFIER)) throw new IllegalArgumentException(
				"Expected identifier " + ClientCommunicationMessage.IDENTIFIER + ", got " + input.identifier);
		return decrypt((EncryptedMessage) input, encryptions);
	}
}
