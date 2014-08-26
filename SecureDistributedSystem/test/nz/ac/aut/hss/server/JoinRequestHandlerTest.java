package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.server.JoinRequestHandler;
import nz.ac.aut.hss.distribution.server.KeyAuthority;
import nz.ac.aut.hss.distribution.server.ProcessingException;
import nz.ac.aut.hss.util.ECCKeyGen;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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
	public void twoStep() throws ProcessingException {
		handshake();
		final String nonce = "1";
		Message msg = handler.processInput("1", new ClientInformationMessage("12345", ECCKeyGen.create(), nonce));
		assertTrue(msg instanceof SessionMessage);
		assertNotNull(((SessionMessage) msg).sessionKey);
		assertEquals(nonce, ((SessionMessage) msg).nonce);
	}
}
