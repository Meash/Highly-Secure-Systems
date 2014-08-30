package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.MessageEncrypter;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.SimpleTextMessage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class MessageEncrypterTest {
	private MessageEncrypter encrypter;

	@Before
	public void setUp() {
		encrypter = new MessageEncrypter();
	}

	@Test
	public void simpleTextReverse() throws CryptException, IOException, ClassNotFoundException {
		final Message source = new SimpleTextMessage("some text 123!");
		final EncryptedMessage encrypted = encrypter.applyEncryptions(source);
		final Message actual = encrypter.decrypt(encrypted);
		assertEquals(source, actual);
	}
}
