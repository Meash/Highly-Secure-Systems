package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class EncryptedMessage extends SimpleTextMessage {
	public EncryptedMessage(final String identifier, final String content) {
		super(identifier, content);
	}

	@Override
	public Encryption[] getEncryptions() {
		throw new UnsupportedOperationException("Encryptions are not stored in encrypted message");
	}
}
