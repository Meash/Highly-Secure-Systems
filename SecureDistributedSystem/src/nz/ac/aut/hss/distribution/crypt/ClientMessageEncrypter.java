package nz.ac.aut.hss.distribution.crypt;

import nz.ac.aut.hss.distribution.protocol.*;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientMessageEncrypter extends MessageEncrypter {
	private final PrivateKey privateKey;
	private final Map<String, SecretKey> clientSessionKeys;

	public ClientMessageEncrypter(final PrivateKey privateKey) {
		if (privateKey == null)
			throw new IllegalArgumentException("private key is null");
		this.privateKey = privateKey;
		this.clientSessionKeys = new HashMap<>();
	}

	public Message decrypt(final Message input, final String phone)
			throws IOException, CryptException, ClassNotFoundException {
		if (!(input instanceof EncryptedMessage))
			return input;

		final Encryption[] encryptions;
		switch (input.identifier) {
			case JoinConfirmationMessage.IDENTIFIER:
			case CommunicationRequestMessage.IDENTIFIER:
				encryptions = new Encryption[]{new RSA(privateKey, null)};
				break;
			case CommunicationConfirmationMessage.IDENTIFIER:
				final SecretKey sessionKey = clientSessionKeys.get(phone);
				if (sessionKey == null)
					throw new IllegalStateException("Session key for phone '" + phone + "' has not been set");
				try {
					encryptions = new Encryption[]{new AES(sessionKey)};
				} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
					throw new CryptException(e);
				}
				break;
			default:
				throw new IllegalArgumentException("Unsupported identifier '" + input.identifier + "'");
		}
		return decrypt((EncryptedMessage) input, encryptions);
	}

	public void putSessionKey(final String phone, final SecretKey key) {
		this.clientSessionKeys.put(phone, key);
	}
}
