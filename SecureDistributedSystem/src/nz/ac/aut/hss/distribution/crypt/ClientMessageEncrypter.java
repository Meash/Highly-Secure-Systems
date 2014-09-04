package nz.ac.aut.hss.distribution.crypt;

import nz.ac.aut.hss.distribution.protocol.*;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientMessageEncrypter extends MessageEncrypter {
	private final PrivateKey privateKey;
	private SecretKey sessionKey;

	public ClientMessageEncrypter(final PrivateKey privateKey) {
		if (privateKey == null)
			throw new IllegalArgumentException("private key is null");
		this.privateKey = privateKey;
	}

	public void setSessionKey(final SecretKey sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Message decrypt(final Message input)
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
				if (sessionKey == null)
					throw new IllegalStateException("sessionKey has not been set");
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
}
