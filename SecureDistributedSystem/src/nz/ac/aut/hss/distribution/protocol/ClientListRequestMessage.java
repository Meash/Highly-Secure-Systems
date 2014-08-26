package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientListRequestMessage extends SimpleTextMessage {
	public ClientListRequestMessage() {
		super("Client list please");
	}
}
