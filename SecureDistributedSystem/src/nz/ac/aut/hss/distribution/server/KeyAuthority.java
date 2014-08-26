package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.Base64Coder;
import nz.ac.aut.hss.distribution.util.ObjectFileStore;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class KeyAuthority {
	private static final Path CLIENT_SESSION_FILE = Paths.get("sessions.obj");
	private static final Path CLIENT_PUBLICKEY_FILE = Paths.get("clients-publickeys.obj");

	private final Map<Class<? extends Message>, RequestHandler> requestAssignments = new HashMap<>();

	private final ObjectSerializer serializer;
	private RequestHandler handler;
	/** Phone -> public key */
	private final Map<String, ECPublicKey> clientPublicKeys;
	/** Client ID -> session key */
	private final Map<String, SecretKey> clientSessionKeys;

	public KeyAuthority() throws IOException, ClassNotFoundException {
		requestAssignments.put(JoinRequestMessage.class, new JoinRequestHandler(this));
		requestAssignments.put(ClientListRequestMessage.class, new ClientListRequestHandler(this));
		requestAssignments.put(PublicKeyMessage.class, new PublicKeyRequestHandler(this));

		serializer = new ObjectSerializer();
		clientPublicKeys = loadMap(CLIENT_PUBLICKEY_FILE);
		clientSessionKeys = loadMap(CLIENT_SESSION_FILE);
		// TODO store session keys? ports change on reboot
		// - could put phone number in every message instead
	}

	private Map loadMap(Path filePath) throws IOException, ClassNotFoundException {
		if (Files.exists(filePath)) {
			Object clientListObject = new ObjectFileStore(filePath).retrieve();
			if (!(clientListObject instanceof Map))
				throw new IllegalStateException("Client list is of the wrong class");
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
	public String processInput(final String clientId, final String inputLine) throws ProcessingException, IOException {
		final Object inputObject = deserializeInput(inputLine);
		final Message input = validateMessage(inputObject);

		if (handler == null) {
			handler = requestAssignments.get(input.getClass());
		}
		Message outputMessage = handler.processInput(clientId, input);

		if (outputMessage == null)
			return null;
		if ((outputMessage instanceof SuppressedMessage))
			return "";
		final String serial = serializer.serialize(outputMessage);
		return applyEncryptions(clientId, serial, outputMessage.encryptions);
	}

	private String applyEncryptions(final String clientId, final String serial,
									final Message.EncryptionMode[] encryptions) {
		String output = serial;
		for (Message.EncryptionMode encryption : encryptions) {
			switch (encryption) {
				case BASE64:
					output = Base64Coder.encodeString(output);
					break;
				case SESSION_KEY:
					final SecretKey key = clientSessionKeys.get(clientId);
					break;
				case CLIENT_PUBLIC_KEY:
					break;
			}
		}
		return output;
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
		new ObjectFileStore(CLIENT_SESSION_FILE).store(clientSessionKeys);
	}

	public Map<String, ECPublicKey> getClientPublicKeys() {
		return clientPublicKeys;
	}

	public void addClientPublicKey(final String phone, final ECPublicKey publicKey) {
		clientPublicKeys.put(phone, publicKey);
	}

	public void addClientSessionKey(final String clientId, final SecretKey secretKey) {
		clientSessionKeys.put(clientId, secretKey);
	}
}
