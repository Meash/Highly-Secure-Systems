package nz.ac.aut.hss.client;

import nz.ac.aut.hss.distribution.protocol.Message;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientCommunication {
	/**
	 * @param partnerPhoneNumber the phone number of the partner we are connecting to
	 * @throws InstantiationException if the communication could not be established
	 */
	public ClientCommunication(final String partnerPhoneNumber) throws InstantiationException {
		// TODO: init communication

	}

	public void sendMessage(final String message, final boolean confidential, final boolean authenticate) {
		
	}

	public boolean isMessageConfidential(final Message message) {
		return true;
	}

	public boolean isMessageAuthentic(final Message message) {
		return true;
	}
}
