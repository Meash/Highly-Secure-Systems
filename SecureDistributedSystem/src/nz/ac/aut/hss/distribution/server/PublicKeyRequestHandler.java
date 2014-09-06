package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.*;

import java.security.PublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class PublicKeyRequestHandler implements RequestHandler {
	private final KeyAuthority authority;

	public PublicKeyRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
	}

	@Override
	public Message processInput(final String clientId, final Message input) {
		if (!(input instanceof ClientRequestMessage))
			return new ProtocolInvalidationMessage(
					"Expected message of class " + ClientRequestMessage.class.getName() + ", got " +
							input.getClass().getName());
		final ClientRequestMessage request = (ClientRequestMessage) input;
		final Map<String, PublicKey> phonePublicKey = authority.getClientPublicKeys();
		final PublicKey publicKey = phonePublicKey.get(request.phone);
		if (publicKey == null) {
			System.out.println("ClientDoesNotExistMessage (" + request.phone + ") to " + clientId);
			return new ClientDoesNotExistMessage(request.phone);
		}
		System.out.println("Client public key (" + request.phone + ") to " + clientId);
		return new ClientPublicKeyMessage(request.phone, publicKey);
	}
}
