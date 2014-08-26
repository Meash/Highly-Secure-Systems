package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.ClientListMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.ProtocolInvalidationMessage;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ClientListRequestHandler implements RequestHandler {
	private final KeyAuthority authority;

	public ClientListRequestHandler(final KeyAuthority authority) {
		this.authority = authority;
	}

	@Override
	public Message processInput(final Message input) {
		if (!(input instanceof ClientListMessage))
			return new ProtocolInvalidationMessage(
					"Expected message of class " + ClientListMessage.class.getName() + ", got " +
							input.getClass().getName());
		return new ClientListMessage(authority.getClientList());
	}
}
