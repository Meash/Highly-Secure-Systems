package nz.ac.aut.hss.distribution.protocol;

import nz.ac.aut.hss.distribution.crypt.Encryption;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class EncryptedMessage extends SimpleTextMessage {
	public EncryptedMessage(final String content, final Encryption... encryptions) {
		super(content, encryptions);
	}
}
