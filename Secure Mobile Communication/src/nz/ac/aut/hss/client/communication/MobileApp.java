package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface MobileApp {
	public String getOneTimePassword();

	public String getPhoneNumber();

	public void displayError(final String message, final Throwable tr);
}
