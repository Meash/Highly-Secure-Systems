package nz.ac.aut.hss.auth;

import nz.ac.aut.hss.distribution.auth.MessageAuthenticator;
import nz.ac.aut.hss.distribution.crypt.ECCEncryption;
import nz.ac.aut.hss.distribution.protocol.ClientListRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
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
	public void equal()			throws Exception {
		final Message msg = new ClientListRequestMessage();
		final KeyPair keyPair = ECCEncryption.createKeyPair();
		final ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
		final ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
		msg.authentication = authenticator.hash(msg, publicKey);
		assertTrue(authenticator.verify(msg, publicKey, privateKey));
	}

	@Test
	public void diff()			throws Exception {
		final Message msg = new ClientListRequestMessage();
		msg.authentication = "random dummy auth that's not gonna work";
		final KeyPair keyPair = ECCEncryption.createKeyPair();
		final ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
		final ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
		assertFalse(authenticator.verify(msg, publicKey, privateKey));
	}
}
