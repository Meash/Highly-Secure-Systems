package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.Message;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public interface RequestHandler {
	public Message processInput(final String clientId, final Message inputLine) throws ProcessingException;
}
