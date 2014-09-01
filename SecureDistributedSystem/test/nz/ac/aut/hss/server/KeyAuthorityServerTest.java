package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import nz.ac.aut.hss.util.ECCKeyGen;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class KeyAuthorityServerTest {
	private KeyAuthorityServer server;
	private ObjectSerializer serializer;
	private final int port = 61001;

	@Before
	public void setUp() throws IOException, ClassNotFoundException {
		server = new KeyAuthorityServer(port);
		serializer = new ObjectSerializer();
	}

	@Test
	public void sessionMessage() throws IOException, ClassNotFoundException {
		server.start();
		Socket sock = new Socket("localhost", port);

		// receive file
		try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
			out.println(serializer.serialize(new JoinRequestMessage()));
			final String nonce = "somenonce";
			out.println(serializer.serialize(new ClientInformationMessage("12345", ECCKeyGen.create(), nonce)));
			final String line = in.readLine();
			Object msgObj = serializer.deserialize(line);
			assertEquals(EncryptedMessage.class, msgObj.getClass());
			msgObj = serializer.deserialize(((EncryptedMessage) msgObj).content);
			assertEquals(SessionMessage.class, msgObj.getClass());
			assertEquals(nonce, ((SessionMessage) msgObj).nonce);
		}
		sock.close();
	}

	@After
	public void tearDown() {
		server.interrupt();
	}
}
