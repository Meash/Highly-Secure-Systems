package nz.ac.aut.hss.distribution.protocol;

import com.sun.istack.internal.Nullable;
import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.io.Serializable;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public abstract class Message implements Serializable {
	/**
	 * The encryptions that will be applied to the serialized version of this message. The first encryption in the
	 * array will be applied first, the last one last
	 */
	private Encryption[] encryptions;
	/**
	 * The hash code serving as message authentication - can be null for unauthenticated messages. This hash is
	 * computed
	 * on the same message with a null hash
	 */
	@Nullable
	public String authentication;

	protected Message(final Encryption... encryptionIntructions) {
		this(null, encryptionIntructions);
	}

	protected Message(final String authentication, final Encryption... encryptions) {
		this.authentication = authentication;
		this.encryptions = encryptions;
	}

	public Encryption[] getEncryptions() {
		return encryptions;
	}

	public void setEncryptions(final Encryption[] encryptions) {
		if(encryptions == null)
			throw new IllegalArgumentException("Encryptions must not be null, use zero-sized array instead");
		this.encryptions = encryptions;
	}
}
