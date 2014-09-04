package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 05.09.2014
 */
public interface SMSSender {
	public void send(final String phone, final String content);
}
