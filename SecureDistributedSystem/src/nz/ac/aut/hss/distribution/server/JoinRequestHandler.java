package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.crypt.*;
import nz.ac.aut.hss.distribution.protocol.*;

import javax.crypto.SecretKey;
import java.security.PublicKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinRequestHandler implements RequestHandler {
	public static final String CONVEY_MESSAGE = ">> Convey this password confidentially: ";

	private final KeyAuthority authority;
	private final KeyUtil keyUtil;
	private final AsymmetricKeyUtil asymmetricKeyUtil;

	public JoinRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
		keyUtil = new KeyUtil(AES.KEY_ALGORITHM);
		asymmetricKeyUtil = new AsymmetricKeyUtil(RSA.ALGORITHM);
	}


	@Override
	public Message processInput(final String clientId, final Message input) throws ProcessingException {
		try {
			if (input instanceof JoinRequestMessage) {
				final SecretKey key = AES.createKey(128);
				authority.putSessionKey(clientId, key);
				final String password = keyUtil.toString(key);
				System.out.println(CONVEY_MESSAGE + password);
				return new SuppressedMessage();
			} else if (input instanceof ClientInformationMessage) {
				final ClientInformationMessage clientMessage = (ClientInformationMessage) input;
				final PublicKey publicKey = asymmetricKeyUtil.toPublicKey(clientMessage.publicKey);
				authority.putClientPublicKey(clientMessage.telephoneNumber, publicKey);
				System.out.println("Confirmed new client: " + clientId);
				final Encryption encryption = new RSA(publicKey, null); // encrypt with client's public key
				final String encryptedNonce = encryption.encrypt(clientMessage.nonce);
				return new EncryptedJoinConfirmationMessage(encryptedNonce);
			} else {
				throw new IllegalArgumentException("Invalid message " + input.getClass());
			}
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
	}
}
