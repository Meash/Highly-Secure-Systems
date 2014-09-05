package nz.ac.aut.hss.client.communication;

import android.content.Context;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public interface CommunicationDisplay {
	public void addReceivedMessage(final String text, final boolean confidential, final boolean authentic);
}
