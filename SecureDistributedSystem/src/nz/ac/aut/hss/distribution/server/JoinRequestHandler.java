package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ECCEncryption;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.Base64Coder;

import javax.crypto.SecretKey;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinRequestHandler implements RequestHandler {
	public static final String CONVEY_MESSAGE = ">> Convey this password confidentially: ";

	private final KeyAuthority authority;

	public JoinRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
	}


	@Override
	public Message processInput(final String clientId, final Message input) throws ProcessingException {
		try {
			if (input instanceof JoinRequestMessage) {
				final SecretKey key = AES.createKey(128);
				authority.putSessionKey(clientId, key);
				final String password = new String(Base64Coder.encode(key.getEncoded()));
				System.out.println(CONVEY_MESSAGE + password);
				return new SuppressedMessage();
			} else if (input instanceof ClientInformationMessage) {
				final ClientInformationMessage clientMessage = (ClientInformationMessage) input;
				if (clientMessage.telephoneNumber == null)
					return new ProtocolInvalidationMessage("No telephone number provided");
				if (clientMessage.publicKey == null)
					return new ProtocolInvalidationMessage("No public key provided (null)");
				authority.addClientPublicKey(clientMessage.telephoneNumber, clientMessage.publicKey);
				// encrypt with client's public key
				return new JoinConfirmationMessage(clientMessage.nonce, new ECCEncryption(clientMessage.publicKey, null));
			} else {
				throw new IllegalArgumentException("Invalid message " + input.getClass());
			}
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
	}
}
