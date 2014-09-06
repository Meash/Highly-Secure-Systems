package nz.ac.aut.hss.client;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import nz.ac.aut.hss.client.communication.*;
import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.KeyUtil;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
@RunWith(JMockit.class)
public class ServerCommunicationTest {
	private static final int PORT = 61001;
	@Mocked
	private MobileApp app = null;
	@Mocked
	private KeyStore keyStore = null;
	private static KeyPair keyPair;
	private ServerCommunication communication;
	private static KeyAuthorityServer server;
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = RSA.createKeyPair();

		server = new KeyAuthorityServer(PORT);
		server.start();
	}

	@Before
	public void setUp() throws Exception {
		final String password = "ooekefcd4okgvbkwdozlckhnn";
		final SecretKey secretKey = new KeyUtil(AES.KEY_ALGORITHM).toKey(password);
		new NonStrictExpectations(AES.class) {{
			AES.createKey(anyInt);
			result = secretKey;
		}};
		new NonStrictExpectations() {{
			app.getOneTimePassword();
			result = password;

			app.getPhoneNumber();
			result = "12345";

			keyStore.loadOrCreateAndSaveKeyPair();
			result = keyPair;
		}};

		communication = new ServerCommunication("localhost", PORT, app, keyStore);
	}

	@Test
	public void requestJoin() throws Exception {
		communication.requestJoin();
	}

	@Test
	public void clientList() throws Exception {
		Map map = communication.requestList();
		assertTrue(map.isEmpty());
	}

	@Test
	public void nonExistentClient() throws Exception {
		exception.expect(ClientDoesNotExistException.class);
		communication.requestClient("this isn't even a phone number");
	}

	@AfterClass
	public static void tearDownAfterClass() {
		if (server != null)
			server.interrupt();
	}
}
