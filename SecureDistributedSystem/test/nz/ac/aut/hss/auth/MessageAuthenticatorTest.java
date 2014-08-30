package nz.ac.aut.hss.auth;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.protocol.ClientListRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.util.ECCKeyGen;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class MessageAuthenticatorTest {
	private MessageAuthenticator authenticator;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		authenticator = new MessageAuthenticator();
	}

	@Test
	public void equal() throws IOException {
		final Message msg = new ClientListRequestMessage();
		final ECPublicKey key = ECCKeyGen.create();
		msg.authentication = authenticator.hash(msg, key);
		assertTrue(authenticator.verify(msg, key, ECCKeyGen.privateKey()));
	}

	@Test
	public void diff() throws IOException {
		final Message msg = new ClientListRequestMessage();
		msg.authentication = "random dummy auth that's not gonna work";
		assertFalse(authenticator.verify(msg, ECCKeyGen.create(), ECCKeyGen.privateKey()));
	}
}