package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface SMSReceiver {
	/**
	 * Adds a listener that listens to <b>all</b> incoming SMS.
	 * @param listener the listener
	 */
	public void addListener(SMSListener listener);
	/**
	 * @param phoneNumber the phone number this listener is listening to
	 * @param listener    the listener
	 */
	public void addListener(final String phoneNumber, SMSListener listener);
}
