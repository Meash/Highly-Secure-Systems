package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientListRequestMessage extends SimpleTextMessage {
	public static final String IDENTIFIER = "client_list_request";

	public ClientListRequestMessage() {
		super(IDENTIFIER, "Client list please");
	}
}
