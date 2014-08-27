package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.protocol.ClientInformationMessage;
import nz.ac.aut.hss.distribution.protocol.JoinRequestMessage;
import nz.ac.aut.hss.distribution.protocol.SessionMessage;
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
	public void test() throws IOException, ClassNotFoundException {
		server.start();
		Socket sock = new Socket("localhost", port);

		// receive file
		try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
			out.println(serializer.serialize(new JoinRequestMessage()));
			final String nonce = "somenonce";
			out.println(serializer.serialize(new ClientInformationMessage("12345", ECCKeyGen.create(), nonce)));
			String line = in.readLine();
			Object msg = new ObjectSerializer().deserialize(line);
			assertEquals(SessionMessage.class, msg.getClass());
			assertEquals(nonce, ((SessionMessage) msg).nonce);
		}
		sock.close();
	}

	@After
	public void tearDown() {
		server.interrupt();
	}
}
