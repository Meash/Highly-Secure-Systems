package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.Message;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public interface RequestHandler {
	public Message processInput(Message inputLine) throws ProcessingException;
}
