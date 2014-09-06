package nz.ac.aut.hss.client.communication;

import nz.ac.aut.hss.distribution.crypt.*;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
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
	private final KeyPair keyPair;
	private final KeyUtil keyUtil;

	public ServerCommunication(final String server, final int port, final MobileApp app, final KeyPair keyPair)
			throws IOException, KeyStoreException {
		this.app = app;
		this.phoneNumber = app.getPhoneNumber();

		sock = new Socket(server, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream(), true);

		serializer = new ObjectSerializer();
		keyUtil = new KeyUtil(AES.KEY_ALGORITHM);

		this.keyPair = keyPair;
	}

	public void requestJoin() throws CommunicationException, InterruptedException {
		requestJoin1();
		requestJoin2();
	}

	public void requestJoin1() throws CommunicationException, InterruptedException {
		try {
			/* step 1/2: initial request */
			send(new JoinRequestMessage());
		} catch (IOException e) {
			throw new CommunicationException(e);
		}
	}

	public void requestJoin2() throws CommunicationException, InterruptedException {
		try {
			/* step 2/2: confirm one-time password, send client info */
			final String oneTimePassword = app.getOneTimePassword();
			final SecretKey key = keyUtil.toKey(oneTimePassword);
			final AES symmetricEncryption = new AES(key);

			final PublicKey publicKey = keyPair.getPublic();
			final String nonce = RandomStringUtils.randomAlphanumeric(Message.NONCE_LENGTH);
			final ClientInformationMessage clientInfoMsg =
					new ClientInformationMessage(phoneNumber, publicKey, nonce, symmetricEncryption);
			send(clientInfoMsg);

			Object msgObj = readObject();
			if (!(msgObj instanceof EncryptedJoinConfirmationMessage)) {
				throw new CommunicationException(
						"Invalid reply to request - expected EncryptedJoinConfirmationMessage, got " +
								msgObj.getClass().getName());
			}
			final EncryptedJoinConfirmationMessage msg = (EncryptedJoinConfirmationMessage) msgObj;
			final Encryption asymmetricEncryption = new RSA(null, keyPair.getPrivate());
			final String decryptedNonce = asymmetricEncryption.decrypt(msg.encryptedNonce);
			if (!decryptedNonce.equals(nonce))
				throw new CommunicationException("Invalid nonce reply");
		} catch (CryptException | IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new CommunicationException(e);
		}
	}

	/**
	 * @return a map from the client's telephone number to its public key
	 */
	public Map<String, PublicKey> requestList() throws CommunicationException, InterruptedException {
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
			throws CommunicationException, ClientDoesNotExistException, InterruptedException {
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

	private void send(final Message msg) throws IOException, InterruptedException {
		final SavedException<IOException> saved = new SavedException<>();
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					out.println(serializer.serialize(msg));
				} catch (IOException e) {
					saved.save(e);
				}
			}
		};
		thread.start();
		thread.join();
		saved.throwIfSaved();
	}

	private Object readObject() throws IOException, ClassNotFoundException {
		final String line = in.readLine();
		return serializer.deserialize(line);
	}
}
