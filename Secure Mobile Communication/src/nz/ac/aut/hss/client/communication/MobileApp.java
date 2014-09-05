package nz.ac.aut.hss.client.communication;

import android.content.Context;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface MobileApp {
	public String getOneTimePassword();

	public String getPhoneNumber();

	public void displayError(final String message);

	/**
	 * @param phoneNumber the partner's phone number
	 * @return the communication display if accepts, null if not
	 */
	public CommunicationDisplay accept(final String phoneNumber);

}
