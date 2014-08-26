package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ClientDoesNotExistMessage extends SimpleTextMessage {
	public ClientDoesNotExistMessage(final String phone) {
		super(phone);
	}
}
