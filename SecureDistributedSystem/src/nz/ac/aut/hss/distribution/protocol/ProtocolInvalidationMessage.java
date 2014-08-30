package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ProtocolInvalidationMessage extends Message {
	public final String message;

	public ProtocolInvalidationMessage(final String message, final Encryption... encryptions) {
		super(encryptions);
		this.message = message;
	}
}
