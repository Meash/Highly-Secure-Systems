package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.ClientDoesNotExistMessage;
import nz.ac.aut.hss.distribution.protocol.ClientPublicKeyMessage;
import nz.ac.aut.hss.distribution.protocol.ClientRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.server.KeyAuthority;
import nz.ac.aut.hss.distribution.server.PublicKeyRequestHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class PublicKeyRequestHandlerTest {
	private PublicKeyRequestHandler handler;
	private KeyAuthority authority;

	@Before
	public void setUp() throws IOException, ClassNotFoundException {
		authority = new KeyAuthority();
		handler = new PublicKeyRequestHandler(authority);
	}

	@Test
	public void clientDoesNotExist() {
		final String phone = "123456";
		Message msg = handler.processInput("1", new ClientRequestMessage(phone));
		assertTrue(msg instanceof ClientDoesNotExistMessage);
	}

	@Test
	public void singleClient() throws Exception {
		final String phone = "123456";
		final PublicKey publicKey = RSA.createKeyPair().getPublic();
		authority.addClientPublicKey(phone, publicKey);
		Message msg = handler.processInput("1", new ClientRequestMessage(phone));
		assertTrue(msg instanceof ClientPublicKeyMessage);
		assertEquals(phone, ((ClientPublicKeyMessage) msg).phone);
		assertNotNull(((ClientPublicKeyMessage) msg).publicKey);
	}
}
