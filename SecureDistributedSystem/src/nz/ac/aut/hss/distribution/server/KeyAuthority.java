package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.ObjectFileStore;

import java.io.IOException;
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
	private final Map<Class<? extends Message>, RequestHandler> requestAssignments = new HashMap<>();

	private static final Path CLIENT_LIST_FILE = Paths.get("clients.obj");

	private RequestHandler handler;
	private final ObjectFileStore objectStore;
	private final Map<String, ECPublicKey> clientList;

	public KeyAuthority() throws IOException, ClassNotFoundException {
		requestAssignments.put(JoinRequestMessage.class, new JoinRequestHandler(this));
		requestAssignments.put(ClientRequestMessage.class, new ClientListRequestHandler(this));
		requestAssignments.put(PublicKeyMessage.class, new PublicKeyRequestHandler(this));

		objectStore = new ObjectFileStore(CLIENT_LIST_FILE);
		clientList = loadClientList();
	}

	private Map<String, ECPublicKey> loadClientList() throws IOException, ClassNotFoundException {
		if (Files.exists(CLIENT_LIST_FILE)) {
			Object clientListObject = objectStore.retrieve();
			if (!(clientListObject instanceof Map))
				throw new IllegalStateException("Client list is of the wrong class");
			return (Map<String, ECPublicKey>) clientListObject;
		} else {
			return new HashMap<>();
		}
	}

	/**
	 * @param input the input
	 * @return the output message or null to terminate the connection
	 */
	public Message processInput(final Message input) throws ProcessingException {
		if (handler == null) {
			handler = requestAssignments.get(input.getClass());
		}
		return handler.processInput(input);
	}

	public void storeClientList() throws IOException {
		objectStore.store(clientList);
	}

	public Map<String, ECPublicKey> getClientList() {
		return clientList;
	}

	public void addClient(final String phone, final ECPublicKey publicKey) {
		clientList.put(phone, publicKey);
	}
}
