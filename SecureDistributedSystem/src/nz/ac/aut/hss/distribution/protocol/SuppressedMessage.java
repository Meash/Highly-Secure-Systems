package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SuppressedMessage extends Message {
	public SuppressedMessage(final Encryption... encryptions) {
		super(encryptions);
	}
}
