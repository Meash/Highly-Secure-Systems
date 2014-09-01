package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ECCEncryption;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.PasswordGenerator;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinRequestHandler implements RequestHandler {
	private static final int PASSWORD_MIN_LENGTH = 6, PASSWORD_MAX_LENGTH = 8;
	private static final String ENCRYPTION = "AES";

	private final KeyAuthority authority;

	public JoinRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
	}


	@Override
	public Message processInput(final String clientId, final Message input) throws ProcessingException {
		try {
			if (input instanceof JoinRequestMessage) {
				final SecretKey key = AES.createKey(128);
				final String password = new String(key.getEncoded(), AES.CHARSET);
				System.out.println(">> Convey this password confidentially: " + password);
				return new SuppressedMessage();
			} else if (input instanceof ClientInformationMessage) {
				final ClientInformationMessage clientMessage = (ClientInformationMessage) input;
				if (clientMessage.telephoneNumber == null)
					return new ProtocolInvalidationMessage("No telephone number provided");
				if (clientMessage.publicKey == null)
					return new ProtocolInvalidationMessage("No public key provided (null)");
				authority.addClientPublicKey(clientMessage.telephoneNumber, clientMessage.publicKey);
				final SecretKey sessionKey = PasswordGenerator.generateSecretKey(ENCRYPTION);
				authority.addClientSessionKey(clientId, sessionKey);
				return new SessionMessage(sessionKey, clientMessage.nonce, new ECCEncryption(null)); // TODO
			} else {
				throw new IllegalArgumentException("Invalid message " + input.getClass());
			}
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
	}
}
