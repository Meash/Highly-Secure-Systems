package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientRequestMessage extends SimpleTextMessage {
	public ClientRequestMessage() {
		super("Client list please");
	}
}
