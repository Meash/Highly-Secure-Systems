package nz.ac.aut.hss.server;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.KeyUtil;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.ClientInformationMessage;
import nz.ac.aut.hss.distribution.protocol.EncryptedJoinConfirmationMessage;
import nz.ac.aut.hss.distribution.protocol.JoinRequestMessage;
import nz.ac.aut.hss.distribution.server.JoinRequestHandler;
import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

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
	public void sessionMessage() throws Exception {
		server.start();
		try (Socket sock = new Socket("localhost", port)) {
			// receive file
			try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
				// redirect console output
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
				PrintStream old = System.out; // Save the old System.out!
				System.setOut(ps);

				/* step 1/2: initial request */
				out.println(serializer.serialize(new JoinRequestMessage()));

				/* step 2/2: confirm one-time password, send client info */
				Thread.sleep(500); // wait for other process
				System.out.flush();
				System.setOut(old); // restore console output
				final String consoleInput = baos.toString();
				final String oneTimePassword = consoleInput.substring(JoinRequestHandler.CONVEY_MESSAGE.length(),
						consoleInput.length() - 2);
				final SecretKey secretKeySpec = new KeyUtil(AES.KEY_ALGORITHM).toKey(oneTimePassword);
				final AES encryption = new AES(secretKeySpec);

				final String nonce = "somenonce";
				final KeyPair keyPair = RSA.createKeyPair();
				final PrivateKey privateKey = keyPair.getPrivate();
				final PublicKey publicKey = keyPair.getPublic();

				final ClientInformationMessage clientInfoMsg =
						new ClientInformationMessage("12345", publicKey, nonce, encryption);
				out.println(serializer.serialize(clientInfoMsg));

				final String line = in.readLine();
				Object msgObj = serializer.deserialize(line);
				assertEquals(EncryptedJoinConfirmationMessage.class, msgObj.getClass());
				final String decryptedNonce =
						new RSA(null, privateKey).decrypt(((EncryptedJoinConfirmationMessage) msgObj).encryptedNonce);
				assertEquals(nonce, decryptedNonce);
			}
		}
	}

	@After
	public void tearDown() {
		server.interrupt();
	}


}
