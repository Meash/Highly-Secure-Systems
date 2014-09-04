package nz.ac.aut.hss.auth;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.ClientListRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class MessageAuthenticatorTest {
	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	private MessageAuthenticator authenticator;

	@BeforeClass
	public static void setUpKeys() throws NoSuchAlgorithmException {
		final KeyPair keyPair = RSA.createKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		authenticator = new MessageAuthenticator();
	}

	@Test
	public void equal() throws Exception {
		final Message msg = new ClientListRequestMessage();
		msg.authentication = authenticator.hash(msg, privateKey);
		assertTrue(authenticator.verify(msg, publicKey));
	}

	@Test
	public void diff() throws Exception {
		final Message msg = new ClientListRequestMessage();
		msg.authentication = "random dummy auth that's not gonna work";
		assertFalse(authenticator.verify(msg, publicKey));
	}
}
