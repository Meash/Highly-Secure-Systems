package nz.ac.aut.hss.client.communication;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.MessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.CommunicationRequestMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
@RunWith(JMockit.class)
public class CommunicationAwaiterTest {
	private static ObjectSerializer serializer;
	private static PrivateKey ownPrivateKey;
	private static PublicKey ownPublicKey;
	private static PublicKey otherPublicKey;
	private CommunicationAwaiter awaiter;
	@Mocked
	private MobileApp app;
	@Mocked
	private SMSReceiver smsReceiver;
	@Mocked
	private SMSSender smsSender;
	private MessageEncrypter encrypter;

	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchAlgorithmException {
		serializer = new ObjectSerializer();
		KeyPair ownKeyPair = RSA.createKeyPair();
		ownPrivateKey = ownKeyPair.getPrivate();
		ownPublicKey = ownKeyPair.getPublic();
		KeyPair otherKeyPair = RSA.createKeyPair();
		otherPublicKey = otherKeyPair.getPublic();
	}

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		awaiter = new CommunicationAwaiter(app, ownPrivateKey, smsReceiver, smsSender);
		encrypter = new ClientMessageEncrypter(ownPrivateKey);
	}

	@Test
	public void properCommunicationRequest() throws Exception {
		final String phone = "123", nonce = "456";
		final SecretKey sessionKey = AES.createKey(128);
		Message msg = new CommunicationRequestMessage(nonce, sessionKey, ownPublicKey, new RSA(otherPublicKey, null));
		msg = encrypter.applyEncryptions(msg);
		awaiter.receive(phone, serializer.serialize(msg));
		new NonStrictExpectations() {{
			smsReceiver.addListener(phone, (PassiveClientCommunication) any);
			times = 1;
		}};
	}
}
