package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.crypt.AsymmetricKeyUtil;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.server.JoinRequestHandler;
import nz.ac.aut.hss.distribution.server.KeyAuthority;
import nz.ac.aut.hss.distribution.server.ProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class JoinRequestHandlerTest {
	private JoinRequestHandler handler;

	@Before
	public void setUp() throws IOException, ClassNotFoundException {
		handler = new JoinRequestHandler(new KeyAuthority());
	}

	@Test
	public void handshake() throws ProcessingException {
		Message msg = handler.processInput("1", new JoinRequestMessage());
		assertEquals(SuppressedMessage.class, msg.getClass());
	}

	@Test
	public void twoStep() throws Exception {
		handshake();
		final String nonce = "1";
		final KeyPair keyPair = RSA.createKeyPair();
		final PublicKey publicKey = keyPair.getPublic();
		final String publicKeyString = new AsymmetricKeyUtil(RSA.ALGORITHM).toString(publicKey);
		Message msg = handler.processInput("1", new ClientInformationMessage("12345", publicKeyString, nonce));
		assertThat(msg, instanceOf(EncryptedJoinConfirmationMessage.class));
		final String decryptedNonce =
				new RSA(null, keyPair.getPrivate()).decrypt(((EncryptedJoinConfirmationMessage) msg).encryptedNonce);
		assertEquals(nonce, decryptedNonce);
	}
}
