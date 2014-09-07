package nz.ac.aut.hss.client.communication;

import java.util.ArrayList;

/**
 * @author Martin Schrimpf
 * @created 05.09.2014
 */
public interface SMSSender {
	public void send(String phone, String content);
}
