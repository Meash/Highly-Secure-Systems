package nz.ac.aut.hss.distribution.protocol;

import java.io.Serializable;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public abstract class Message implements Serializable {
	public final EncryptionMode[] encryptions;

	protected Message(final EncryptionMode... encryptions) {
		this.encryptions = encryptions;
	}

	public enum EncryptionMode {
		BASE64, SESSION_KEY, CLIENT_PUBLIC_KEY
	}
}
