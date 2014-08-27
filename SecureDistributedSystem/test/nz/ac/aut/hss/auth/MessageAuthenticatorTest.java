package nz.ac.aut.hss.auth;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.protocol.ClientListRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
		msg.authentication = authenticator.hash(msg);
		assertTrue(authenticator.verify(msg));
	}

	@Test
	public void diff() throws IOException {
		final Message msg = new ClientListRequestMessage();
		msg.authentication = "random dummy auth that's not gonna work";
		assertFalse(authenticator.verify(msg));
	}
}
