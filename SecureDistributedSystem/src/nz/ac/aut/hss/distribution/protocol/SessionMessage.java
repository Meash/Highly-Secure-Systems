package nz.ac.aut.hss.distribution.protocol;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class SessionMessage extends Message {
	public final String sessionKey;
	public final String nonce;

	public SessionMessage(final String sessionKey, final String nonce) {
		super();
		this.sessionKey = sessionKey;
		this.nonce = nonce;
	}
}
