package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.*;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.SimpleTextMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class MessageEncrypterTest {
	private static final String IDENTIFIER = "test_identifier";
	private MessageEncrypter encrypter;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		encrypter = new MessageEncrypter();
	}

	@Test
	public void plainTextReverse() throws CryptException, IOException, ClassNotFoundException {
		final Message source = new SimpleTextMessage(IDENTIFIER, "some text 123!");
		testReverse(source);
	}

	@Test
	public void base64TextReverse() throws CryptException, IOException, ClassNotFoundException {
		final Message source = new SimpleTextMessage(IDENTIFIER, "some text 123!", new Base64Encryption());
		testReverse(source);
	}

	@Test
	public void aesTextReverse() throws CryptException, IOException, ClassNotFoundException, NoSuchProviderException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		final Message source = new SimpleTextMessage(IDENTIFIER, "some text 123!", new AES(AES.createKey(128)));
		testReverse(source);
	}

	@Test
	public void base64AesTextReverse()
			throws CryptException, IOException, ClassNotFoundException, NoSuchProviderException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		final Message source =
				new SimpleTextMessage(IDENTIFIER, "some text 123!", new Base64Encryption(), new AES(AES.createKey(128)));
		testReverse(source);
	}

	@Test
	public void noEncryptionsInEncryptedMessage() {
		final EncryptedMessage msg = new EncryptedMessage(IDENTIFIER, "123");
		expectedException.expect(UnsupportedOperationException.class);
		msg.getEncryptions();
	}

	private void testReverse(final Message source) throws CryptException, IOException, ClassNotFoundException {
		final Encryption[] encryptions = source.getEncryptions();
		final EncryptedMessage encrypted = encrypter.applyEncryptions(source);
		final Message actual = encrypter.decrypt(encrypted, encryptions);
		assertEquals(source, actual);
	}
}
