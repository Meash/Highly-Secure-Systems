package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.*;
import nz.ac.aut.hss.distribution.util.ObjectFileStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class KeyAuthority {
	private final Map<Message, RequestHandler> requestAssignments = new HashMap<>();

	private static final Path CLIENT_LIST_FILE = Paths.get("clients.obj");

	private RequestHandler handler;
	private final ObjectFileStore objectStore;
	private final Map<String, String> clientList;

	public KeyAuthority() throws IOException, ClassNotFoundException {
		requestAssignments.put(Message.JOIN_REQUEST, new JoinRequestHandler());
		requestAssignments.put(Message.REQUEST_CLIENT_LIST, new ClientListRequestHandler(this));
		requestAssignments.put(Message.REQUEST_PUBLIC_KEY, new PublicKeyRequestHandler(this));

		objectStore = new ObjectFileStore(CLIENT_LIST_FILE);
		clientList = loadClientList();
	}

	private Map<String, String> loadClientList() throws IOException, ClassNotFoundException {
		if (Files.exists(CLIENT_LIST_FILE)) {
			Object clientListObject = objectStore.retrieve();
			if (!(clientListObject instanceof Map))
				throw new IllegalStateException("Client list is of the wrong class");
			return (Map<String, String>) clientListObject;
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
			handler = requestAssignments.get(input);
		}
		return handler.processInput(input);
	}

	public void storeClientList() throws IOException {
		objectStore.store(clientList);
	}

	public Map<String, String> getClientList() {
		return clientList;
	}
}
