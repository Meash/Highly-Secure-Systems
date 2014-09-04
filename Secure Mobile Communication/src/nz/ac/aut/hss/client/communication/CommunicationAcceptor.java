package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface CommunicationAcceptor {
	/**
	 * @param phoneNumber the partner's phone number
	 * @return the communication display if accepts, null if not
	 */
	public CommunicationDisplay accept(final String phoneNumber);
}
