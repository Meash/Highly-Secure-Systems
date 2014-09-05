package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.ClientMessageEncrypter;
import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ServerCommunication {
	private final Socket sock;
	private final BufferedReader in;
	private final PrintWriter out;
	private final ObjectSerializer serializer;
	private final MobileApp app;
	private final String phoneNumber;
	private final ClientMessageEncrypter messageEncrypter;
	private final KeyPair keyPair;

	public ServerCommunication(final String server, final int port, final MobileApp app, final KeyStore keyStore)
			throws IOException, KeyStoreException {
		this.app = app;
		this.phoneNumber = app.getPhoneNumber();

		sock = new Socket(server, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream(), true);

		serializer = new ObjectSerializer();

		this.keyPair = keyStore.loadOrCreateAndSaveKeyPair();
		this.messageEncrypter = new ClientMessageEncrypter(keyPair.getPrivate());
	}

	public void requestJoin() throws CommunicationException {
		try {
			/* step 1/2: initial request */
			send(new JoinRequestMessage());


			/* step 2/2: confirm one-time password, send client info */
			final String oneTimePassword = app.getOneTimePassword();
			final byte[] keyBytes = Base64Coder.decodeString(oneTimePassword).getBytes(Encryption.CHARSET);
			final SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
			messageEncrypter.setSessionKey(secretKeySpec);
			final AES encryption = new AES(secretKeySpec);

			final PublicKey publicKey = keyPair.getPublic();
			final String nonce = RandomStringUtils.randomAlphanumeric(Message.NONCE_LENGTH);
			final ClientInformationMessage clientInfoMsg =
					new ClientInformationMessage(phoneNumber, publicKey, nonce, encryption);
			send(clientInfoMsg);

			Object msgObj = readObject();
			if (!(msgObj instanceof EncryptedMessage)) {
				throw new CommunicationException(
						"Invalid reply to request - expected EncryptedMessage, got " + msgObj.getClass().getName());
			}
			final Message msg = messageEncrypter.decrypt((Message) msgObj);
			if (!(msg instanceof JoinConfirmationMessage))
				throw new CommunicationException("Expected join confirmation, got " + msg.getClass().getName());
			if (!((JoinConfirmationMessage) msg).nonce.equals(nonce))
				throw new CommunicationException("Invalid nonce reply");
		} catch (CryptException e) {
			throw new CommunicationException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return a map from the client's telephone number to its public key
	 */
	public Map<String, PublicKey> requestList() throws CommunicationException {
		try {
			send(new ClientListRequestMessage());
			Object msgObj = readObject();
			if (!(msgObj instanceof ClientListMessage))
				throw new CommunicationException("Expected client list message, got " + msgObj.getClass().getName());
			return ((ClientListMessage) msgObj).phonePublicKey;
		} catch (ClassNotFoundException | IOException e) {
			throw new CommunicationException("Could not retrieve list", e);
		}
	}

	/**
	 * @param telephoneNumber the phone number of the client
	 * @return the public key of the client with the given phone number
	 * @throws ClientDoesNotExistException if the client does not exist
	 */
	public PublicKey requestClient(final String telephoneNumber)
			throws CommunicationException, ClientDoesNotExistException {
		try {
			send(new ClientRequestMessage(telephoneNumber));
			Object msgObj = readObject();
			if (msgObj instanceof ClientDoesNotExistMessage)
				throw new ClientDoesNotExistException();
			if (!(msgObj instanceof ClientPublicKeyMessage))
				throw new CommunicationException(
						"Expected client public key or client does not exist message, got " +
								msgObj.getClass().getName());
			return ((ClientPublicKeyMessage) msgObj).publicKey;
		} catch (ClassNotFoundException | IOException e) {
			throw new CommunicationException("Could not retrieve list", e);
		}
	}

	public void close() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (Exception ignore) {
		}
	}

	private void send(Message msg) throws IOException {
		out.println(serializer.serialize(msg));
	}

	private Object readObject() throws IOException, ClassNotFoundException {
		final String line = in.readLine();
		return serializer.deserialize(line);
	}
}
