package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.server.KeyAuthority;

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
	public Message processInput(final Message input) {
		if (!(input instanceof PublicKeyMessage))
			return new ProtocolInvalidationMessage(
					"Expected message of class " + PublicKeyMessage.class.getName() + ", got " +
							input.getClass().getName());
		final PublicKeyMessage request = (PublicKeyMessage) input;
		final Map<String, String> phonePublicKey = authority.getClientList();
		final String publicKey = phonePublicKey.get(request.phone);
		return new PublicKeyMessage(null, publicKey);
	}
}
