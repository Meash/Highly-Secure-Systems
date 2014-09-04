package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.ServerMessageEncrypter;
import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.ObjectFileStore;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class KeyAuthority {
	private static final Path CLIENT_PUBLICKEY_FILE = Paths.get("clients-publickeys.obj");

	/** identifier -> handler */
	private final Map<String, RequestHandler> requestAssignments = new HashMap<>();

	private final ObjectSerializer serializer;
	private final ServerMessageEncrypter messageEncrypter;
	private RequestHandler handler;
	/** Phone -> public key */
	private final Map<String, PublicKey> clientPublicKeys;
	/** ID -> one-time-pass */
	private final Map<String, SecretKey> clientSessionKeys;

	public KeyAuthority() throws IOException, ClassNotFoundException {
		requestAssignments.put(JoinRequestMessage.IDENTIFIER, new JoinRequestHandler(this));
		requestAssignments.put(ClientListRequestMessage.IDENTIFIER, new ClientListRequestHandler(this));
		requestAssignments.put(ClientPublicKeyMessage.IDENTIFIER, new PublicKeyRequestHandler(this));

		serializer = new ObjectSerializer();
		messageEncrypter = new ServerMessageEncrypter(this);
		//noinspection unchecked
		clientPublicKeys = loadMap(CLIENT_PUBLICKEY_FILE);
		clientSessionKeys = new HashMap<>();
	}

	private Map loadMap(Path filePath) throws IOException, ClassNotFoundException {
		if (Files.exists(filePath)) {
			Object clientListObject = new ObjectFileStore(filePath).retrieve();
			if (!(clientListObject instanceof Map))
				throw new IllegalStateException(
						"Client list is of the wrong class - expected " + Map.class.getName() + ", got " +
								clientListObject.getClass().getName());
			return (Map) clientListObject;
		} else {
			return new HashMap<>();
		}
	}

	/**
	 * @param clientId  the id of the client
	 * @param inputLine the input line
	 * @return the string output or an empty string for no response or null to terminate the connection
	 */
	public String processInput(final String clientId, final String inputLine)
			throws ProcessingException, IOException, CryptException, ClassNotFoundException {
		final Object inputObject = deserializeInput(inputLine);
		Message input = validateMessage(inputObject);
		input = messageEncrypter.decrypt(input, clientId);

		if (handler == null) {
			handler = requestAssignments.get(input.identifier);
		}
		Message outputMessage = handler.processInput(clientId, input);

		if (outputMessage == null)
			return null;
		if ((outputMessage instanceof SuppressedMessage))
			return "";
		try {
			outputMessage = messageEncrypter.applyEncryptions(outputMessage);
			return serializer.serialize(outputMessage);
		} catch (CryptException e) {
			throw new ProcessingException("Could not encrypt message", e);
		}
	}

	private Object deserializeInput(final String inputLine) throws ProcessingException {
		try {
			return serializer.deserialize(inputLine);
		} catch (IOException | ClassNotFoundException e) {
			throw new ProcessingException("Could not deserialize input line", e);
		}
	}

	private Message validateMessage(final Object messageObject) throws ProtocolException {
		if (!(messageObject instanceof Message)) {
			throw new IllegalArgumentException(
					"Expected " + Message.class + " object, got " + messageObject.getClass().getName());
		}
		final Message input = (Message) messageObject;
		if (input instanceof ProtocolInvalidationMessage) {
			throw new ProtocolException(
					"Received protocol invalidation: " + ((ProtocolInvalidationMessage) input).message);
		}
		return input;
	}

	public void storeState() throws IOException {
		new ObjectFileStore(CLIENT_PUBLICKEY_FILE).store(clientPublicKeys);
	}

	public Map<String, PublicKey> getClientPublicKeys() {
		return clientPublicKeys;
	}

	public void addClientPublicKey(final String phone, final PublicKey publicKey) {
		clientPublicKeys.put(phone, publicKey);
	}

	public void putSessionKey(final String id, final SecretKey key) {
		clientSessionKeys.put(id, key);
	}

	public SecretKey getOneTimePass(final String clientId) {
		return clientSessionKeys.get(clientId);
	}

	public PublicKey getPublicKey(final String phone) {
		return clientPublicKeys.get(phone);
	}

	public ServerMessageEncrypter getMessageEncrypter() {
		return messageEncrypter;
	}
}