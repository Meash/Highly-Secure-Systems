package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public abstract class Message {
	public static final Message
			SUPPRESS_MESSAGE = new SuppressedMessage(),
			JOIN_REQUEST = new SingleContentMessage("Knock knock!"),
			REQUEST_CLIENT_LIST = new SingleContentMessage("Client list please"),
			REQUEST_PUBLIC_KEY = new SingleContentMessage("Public key please");
}
