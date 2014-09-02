package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SessionMessage extends Message {
	public final String nonce;

	public SessionMessage(final String nonce, final Encryption... encryptions) {
		super(encryptions);
		this.nonce = nonce;
	}
}
