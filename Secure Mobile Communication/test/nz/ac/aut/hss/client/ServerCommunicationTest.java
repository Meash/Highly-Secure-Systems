package nz.ac.aut.hss.client;

import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public class ServerCommunicationTest {
	private static final int PORT = 61001;
	private ServerCommunication communication;
	private KeyAuthorityServer server;

	@Before
	public void setUp() throws Exception {
		communication = new ServerCommunication("localhost", PORT, new AppDummy());
		server = new KeyAuthorityServer(PORT);
		server.start();
	}

	@Test
	public void test() throws Exception {
		communication.requestJoin();
	}

	@After
	public void tearDown() {
		server.interrupt();
	}
}
