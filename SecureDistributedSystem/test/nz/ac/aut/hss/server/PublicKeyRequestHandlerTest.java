package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.protocol.ClientDoesNotExistMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.PublicKeyMessage;
import nz.ac.aut.hss.distribution.server.KeyAuthority;
import nz.ac.aut.hss.distribution.server.PublicKeyRequestHandler;
import nz.ac.aut.hss.util.ECCKeyGen;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		Message msg = handler.processInput(new PublicKeyMessage(phone, null));
		assertTrue(msg instanceof ClientDoesNotExistMessage);
	}

	@Test
	public void singleClient() {
		final String phone = "123456";
		authority.addClient(phone, ECCKeyGen.create());
		Message msg = handler.processInput(new PublicKeyMessage(phone, null));
		assertTrue(msg instanceof PublicKeyMessage);
		assertEquals(phone, ((PublicKeyMessage) msg).phone);
		assertNotNull(((PublicKeyMessage) msg).publicKey);
	}
}
