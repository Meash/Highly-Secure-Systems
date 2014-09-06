package nz.ac.aut.hss.client.communication;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.ClientCommunicationMessage;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
@RunWith(JMockit.class)
public class ClientCommunicationTest {
	private static ObjectSerializer serializer = new ObjectSerializer();
	private ClientCommunication communication;
	private final String phone = "123";
	@Mocked
	private ServerCommunication serverComm;
	@Mocked
	private SMSSender smsSender;
	private static PrivateKey ownPrivateKey;
	private static PublicKey partnerPublicKey;
	private static PrivateKey partnerPrivateKey;

	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchAlgorithmException {
		final KeyPair keyPair = RSA.createKeyPair();
		ownPrivateKey = keyPair.getPrivate();

		final KeyPair partnerKeyPair = RSA.createKeyPair();
		partnerPublicKey = partnerKeyPair.getPublic();
		partnerPrivateKey = partnerKeyPair.getPrivate();
	}

	@Before
	public void setUp() throws CommunicationException, ClientDoesNotExistException {
		new NonStrictExpectations() {{
			serverComm.requestClient(anyString);
			result = partnerPublicKey;
		}};

		communication = new ClientCommunication(phone, serverComm, smsSender, ownPrivateKey);
	}

	@Test
	public void plainMessage() throws Exception {
		final String msg = "hello there";
		communication.sendMessage(msg, false, false);
		new Verifications() {{
			final String expectedSerial = serializer.serialize(new ClientCommunicationMessage(msg));
			smsSender.send(phone, expectedSerial);
			times = 1;
		}};
	}

	@Test
	public void encryptedMessage() throws Exception {
		final String msgContent = "hello there";
		communication.sendMessage(msgContent, true, false);
		new Verifications() {{
			final String content;
			smsSender.send(phone, content = withCapture());
			times = 1;
			final Object obj = serializer.deserialize(content);
			assertTrue(obj instanceof EncryptedMessage);
			Message msg = (Message) obj;
			assertTrue(communication.isMessageConfidential(msg));
			ClientMessageEncrypter partnerMessageEncrypter = new ClientMessageEncrypter(partnerPrivateKey);
			msg = partnerMessageEncrypter.decrypt(msg);
			assertTrue(msg instanceof ClientCommunicationMessage);
			assertEquals(msgContent, ((ClientCommunicationMessage) msg).content);
		}};
	}
}
