package nz.ac.aut.hss.distribution.server;

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

	private State state = State.AWAITING_REQUEST;
	private final KeyAuthority authority;

	public JoinRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
	}

	private enum State {
		AWAITING_REQUEST, AWAITING_PUBLIC_KEY
	}

	@Override
	public Message processInput(final String clientId, final Message input) throws ProcessingException {
		try {
			switch (state) {
				case AWAITING_REQUEST:
					if (! (input instanceof JoinRequestMessage))
						return new ProtocolInvalidationMessage("Expecting join request, got '" + input + "'");
					final String password =
							PasswordGenerator.generateRandomPassword(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);
					System.out.println("Convey this password confidentially: " + password);
					state = State.AWAITING_PUBLIC_KEY; // only step forward on success
					return new SuppressedMessage();

				case AWAITING_PUBLIC_KEY:
					state = State.AWAITING_REQUEST; // reset in any case
					if (!(input instanceof ClientInformationMessage))
						return new ProtocolInvalidationMessage(
								"Expecting client info reply, got " + input.getClass().getName());
					final ClientInformationMessage clientMessage = (ClientInformationMessage) input;
					if(clientMessage.telephoneNumber == null)
						return new ProtocolInvalidationMessage("No telephone number provided");
					if (clientMessage.publicKey == null)
						return new ProtocolInvalidationMessage("No public key provided (null)");
					authority.addClientPublicKey(clientMessage.telephoneNumber, clientMessage.publicKey);
					final SecretKey sessionKey = PasswordGenerator.generateSecretKey(ENCRYPTION);
					authority.addClientSessionKey(clientId, sessionKey);
					return new SessionMessage(sessionKey, clientMessage.nonce);

				default:
					throw new IllegalStateException();
			}
		} catch (Exception e) {
			state = State.AWAITING_REQUEST; // clear state
			throw new ProcessingException(e);
		}
	}
}
