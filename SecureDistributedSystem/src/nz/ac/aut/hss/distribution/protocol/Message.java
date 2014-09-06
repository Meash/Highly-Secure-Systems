package nz.ac.aut.hss.distribution.protocol;

import com.sun.istack.internal.Nullable;
import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.io.Serializable;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public abstract class Message implements Serializable {
	public static final int NONCE_LENGTH = 10;
	public final String identifier;
	/**
	 * The hash code serving as message authentication - can be null for unauthenticated messages. This hash is
	 * computed
	 * on the same message with a null hash
	 */
	@Nullable
	public String authentication;

	protected Message(final String identifier) {
		this.identifier = identifier;
	}

	public void setEncryptions(final Encryption[] encryptions) {
		if (encryptions == null)
			throw new IllegalArgumentException("Encryptions must not be null, use zero-sized array instead");
	}

	public void setAuthentication(final String authentication) {
		this.authentication = authentication;
	}
}
