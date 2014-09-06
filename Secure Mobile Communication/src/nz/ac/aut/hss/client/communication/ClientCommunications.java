package nz.ac.aut.hss.client.communication;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class ClientCommunications {
	private final Map<String, ClientCommunication> communications;
	private final ServerCommunication serverComm;
	private final SMSSender smsSender;
	private final PrivateKey privateKey;

	public ClientCommunications(final ServerCommunication serverComm, final SMSSender smsSender,
								final PrivateKey privateKey) {
		if(serverComm == null) throw new IllegalArgumentException("serverComm is null");
		this.serverComm = serverComm;
		if(smsSender == null) throw new IllegalArgumentException("smsSender is null");
		this.smsSender = smsSender;
		if(privateKey == null) throw new IllegalArgumentException("privateKey is null");
		this.privateKey = privateKey;
		communications = new HashMap<>();
	}

	public ClientCommunication getOrCreate(final String phone)
			throws CommunicationException, ClientDoesNotExistException, InterruptedException {
		ClientCommunication communication = communications.get(phone);
		if(communication == null) {
			communication = new ClientCommunication(phone, serverComm, smsSender, privateKey);
			communications.put(phone, communication);
		}
		return communication;
	}
}
