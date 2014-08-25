package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public interface RequestHandler {
	public Message processInput(Message inputLine);
}
