package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientDoesNotExistMessage extends SimpleTextMessage {
	private static final String IDENTIFIER = "client_does_not_exist";

	public ClientDoesNotExistMessage(final String phone) {
		super(IDENTIFIER, phone);
	}
}
