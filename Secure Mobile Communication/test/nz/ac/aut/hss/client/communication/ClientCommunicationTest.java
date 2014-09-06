package nz.ac.aut.hss.client.communication;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.ClientCommunicationMessage;
import nz.ac.aut.hss.distribution.protocol.EncryptedClientCommunicationMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
@RunWith(JMockit.class)
public class ClientCommunicationTest {
	private static ObjectSerializer serializer = new ObjectSerializer();
	private ClientCommunication communication;
	private final String partnerPhone = "123";
	@Mocked
	private ServerCommunication serverComm = null;
	@Mocked
	private SMSSender smsSender = null;
	@Mocked
	private ServerCommunication partnerServerComm = null;
	private ClientCommunication partnerCommunication;
	private static PrivateKey ownPrivateKey;
	private static PublicKey ownPublicKey;
	private static PublicKey partnerPublicKey;
	private static PrivateKey partnerPrivateKey;

	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchAlgorithmException {
		final KeyPair keyPair = RSA.createKeyPair();
		ownPrivateKey = keyPair.getPrivate();
		ownPublicKey = keyPair.getPublic();

		final KeyPair partnerKeyPair = RSA.createKeyPair();
		partnerPublicKey = partnerKeyPair.getPublic();
		partnerPrivateKey = partnerKeyPair.getPrivate();
	}

	@Before
	public void setUp() throws CommunicationException, ClientDoesNotExistException {
		new NonStrictExpectations() {{
			serverComm.requestClient(partnerPhone);
			result = partnerPublicKey;
		}};
		communication = new ClientCommunication(partnerPhone, serverComm, smsSender, ownPrivateKey);

		final String ownPhone = "456";
		new NonStrictExpectations() {{
			partnerServerComm.requestClient(ownPhone);
			result = ownPublicKey;
		}};
		partnerCommunication = new ClientCommunication(ownPhone, partnerServerComm, smsSender, partnerPrivateKey);
	}

	@Test
	public void plainMessage() throws Exception {
		final String msg = "hello there";
		communication.sendMessage(msg, false, false);
		new Verifications() {{
			final String expectedSerial = serializer.serialize(new ClientCommunicationMessage(msg));
			smsSender.send(partnerPhone, expectedSerial);
			times = 1;
		}};
	}

	@Test
	public void encryptedMessage() throws Exception {
		final String msgContent = "hello there";
		communication.sendMessage(msgContent, true, false);
		new Verifications() {{
			final String content;
			smsSender.send(partnerPhone, content = withCapture());
			times = 1;
			final Object obj = serializer.deserialize(content);
			assertThat(obj, instanceOf(EncryptedClientCommunicationMessage.class));
			EncryptedClientCommunicationMessage msg = (EncryptedClientCommunicationMessage) obj;
			assertTrue(communication.isMessageConfidential(msg));
			Encryption encryption = new RSA(null, partnerPrivateKey);
			final String decryptedContent = encryption.decrypt(msg.content);
			assertEquals(msgContent, decryptedContent);
		}};
	}

	@Test
	public void authenticatedMessage() throws Exception {
		final String msgContent = "hello there";
		communication.sendMessage(msgContent, false, true);
		new Verifications() {{
			final String content;
			smsSender.send(partnerPhone, content = withCapture());
			times = 1;
			final Object obj = serializer.deserialize(content);
			assertThat(obj, instanceOf(ClientCommunicationMessage.class));
			Message msg = (Message) obj;
			assertNotNull(msg.authentication);
			assertTrue(partnerCommunication.isMessageAuthentic(msg));
		}};
	}

	@Test
	public void encryptedAuthenticatedMessage() throws Exception {
		final String msgContent = "hello there";
		communication.sendMessage(msgContent, true, true);
		new Verifications() {{
			final String content;
			smsSender.send(partnerPhone, content = withCapture());
			times = 1;
			final Object obj = serializer.deserialize(content);
			/* encryption */
			assertThat(obj, instanceOf(EncryptedClientCommunicationMessage.class));
			EncryptedClientCommunicationMessage msg = (EncryptedClientCommunicationMessage) obj;
			assertTrue(communication.isMessageConfidential(msg));
			Encryption encryption = new RSA(null, partnerPrivateKey);
			final String decryptedContent = encryption.decrypt(msg.content);
			assertEquals(msgContent, decryptedContent);
			/* authenticity */
			assertNotNull(msg.authentication);
			assertTrue(partnerCommunication.isMessageAuthentic(msg));
		}};
	}
}
