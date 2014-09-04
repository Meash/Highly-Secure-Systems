package nz.ac.aut.hss.client;

import com.sun.istack.internal.NotNull;
import nz.ac.aut.hss.distribution.crypt.*;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.Base64Coder;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ServerCommunication implements AutoCloseable {
	private static final int NONCE_LENGTH = 10;
	private final Socket sock;
	private final BufferedReader in;
	private final PrintWriter out;
	private final ObjectSerializer serializer;
	private final MobileApp app;
	private final String phoneNumber;
	private ClientMessageEncrypter messageEncrypter;

	public ServerCommunication(final String server, final int port, final MobileApp app) throws IOException {
		sock = new Socket(server, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream(), true);
		serializer = new ObjectSerializer();
		this.app = app;
		this.phoneNumber = app.getPhoneNumber();
	}

	public void requestJoin() throws CommunicationException {
		try {
			/* step 1/2: initial request */
			out.println(serializer.serialize(new JoinRequestMessage()));

			/* step 2/2: confirm one-time password, send client info */
			final String oneTimePassword = app.getOneTimePassword();
			final byte[] keyBytes = Base64Coder.decodeString(oneTimePassword).getBytes(Encryption.CHARSET);
			final SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
			final AES encryption = new AES(secretKeySpec);

			final KeyPair keyPair = RSA.createKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			this.messageEncrypter = new ClientMessageEncrypter(privateKey);

			final PublicKey publicKey = keyPair.getPublic();
			final String nonce = RandomStringUtils.randomAlphanumeric(NONCE_LENGTH);
			final ClientInformationMessage clientInfoMsg =
					new ClientInformationMessage(phoneNumber, publicKey, nonce, encryption);
			out.println(serializer.serialize(clientInfoMsg));

			final String line = in.readLine();
			Object msgObj = serializer.deserialize(line);
			if (!(msgObj instanceof EncryptedMessage)) {
				throw new CommunicationException(
						"Invalid reply to request - expected EncryptedMessage, got " + msgObj.getClass().getName());
			}
			final Message msg = messageEncrypter.decrypt((Message) msgObj, phoneNumber);
			if (!(msg instanceof JoinConfirmationMessage))
				throw new CommunicationException("Expected join confirmation, got " + msg.getClass().getName());
			if (!((JoinConfirmationMessage) msg).nonce.equals(nonce))
				throw new CommunicationException("Invalid nonce reply");
		} catch (IOException | NoSuchPaddingException | ClassNotFoundException | NoSuchAlgorithmException | CryptException e) {
			throw new CommunicationException(e);
		}
	}

	/**
	 * @return true if the join request was successful, false otherwise
	 */
	public boolean sendClientInfo(final String oneTimePass, final ECPublicKey publicKey, final String telephoneNumber) {
		final String nonce = "TODO";
		return true;
	}

	/**
	 * @return a map from the client's telephone number to its public key
	 */
	public Map<String, ECPublicKey> requestList() {
		return null;
	}

	/**
	 * @param telephoneNumber the phone number of the client
	 * @return the public key of the client with the given phone number
	 * @throws ClientDoesNotExistException if the client does not exist
	 */
	@NotNull
	public ECPublicKey requestClient(final String telephoneNumber) throws ClientDoesNotExistException {

		return null;
	}

	@Override
	public void close() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (Exception ignore) {
		}
	}
}
