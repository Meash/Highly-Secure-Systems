package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.ClientDoesNotExistMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.ProtocolInvalidationMessage;
import nz.ac.aut.hss.distribution.protocol.PublicKeyMessage;

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
		if (!(input instanceof PublicKeyMessage))
			return new ProtocolInvalidationMessage(
					"Expected message of class " + PublicKeyMessage.class.getName() + ", got " +
							input.getClass().getName());
		final PublicKeyMessage request = (PublicKeyMessage) input;
		final Map<String, PublicKey> phonePublicKey = authority.getClientPublicKeys();
		final PublicKey publicKey = phonePublicKey.get(request.phone);
		if(publicKey == null)
			return new ClientDoesNotExistMessage(request.phone);
		return new PublicKeyMessage(request.phone, publicKey);
	}
}
