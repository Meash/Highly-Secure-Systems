package nz.ac.aut.hss.distribution.protocol;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public abstract class Message implements Serializable {
	/**
	 * The encryptions that will be applied to the serialized version of this message. The first encryption in the
	 * array
	 * will be applied first, the last one last
	 */
	public final EncryptionMode[] encryptions;
	/**
	 * The hash code serving as message authentication - can be null for unauthenticated messages. This hash is
	 * computed
	 * on the same message with a null hash
	 */
	@Nullable
	public String authentication;

	protected Message(final EncryptionMode... encryptions) {
		this(null, encryptions);
	}

	protected Message(final String authentication, final EncryptionMode... encryptions) {
		this.authentication = authentication;
		this.encryptions = encryptions;
	}

	public enum EncryptionMode {
		BASE64, SESSION_KEY, CLIENT_PUBLIC_KEY
	}
}
