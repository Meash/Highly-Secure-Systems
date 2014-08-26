package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.protocol.JoinRequestMessage;
import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
	public void test() throws IOException {
		server.start();
		Socket sock = new Socket("localhost", port);
		System.out.println("Connecting...");

		// receive file
		try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
			out.write(serializer.serialize(new JoinRequestMessage()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		}
	}

	@After
	public void tearDown() {
		server.interrupt();
	}
}
