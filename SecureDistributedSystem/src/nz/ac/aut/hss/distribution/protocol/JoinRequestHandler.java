package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.util.Utils;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class JoinRequestHandler implements RequestHandler {
	private State state;

	private enum State {
		AWAITING_REQUEST, AWAITING_PUBLIC_KEY
	}

	@Override
	public Message processInput(final Message input) {
		try {
			switch (state) {
				case AWAITING_REQUEST:
					if (!input.equals(Message.JOIN_REQUEST))
						return new ProtocolInvalidationMessage("Expecting join request, got '" + input + "'");
					final String password = Utils.generateRandomPassword();
					System.out.println("Convey this password confidentially: " + password);
					state = State.AWAITING_PUBLIC_KEY;
					return Message.SUPPRESS_MESSAGE;

				case AWAITING_PUBLIC_KEY:
					if (!(input instanceof ClientInformationMessage))
						return new ProtocolInvalidationMessage("Expecting client info reply, got " + input.getClass().getName());
					final ClientInformationMessage clientMessage = (ClientInformationMessage) input;
					final String sessionKey = Utils.generateSymmetricKey();
					state = State.AWAITING_REQUEST;
					return new SessionMessage(sessionKey, clientMessage.nonce);

				default:
					throw new IllegalStateException();
			}
		} catch (Exception e) {
			state = State.AWAITING_REQUEST; // clear state
			throw e;
		}
	}
}
