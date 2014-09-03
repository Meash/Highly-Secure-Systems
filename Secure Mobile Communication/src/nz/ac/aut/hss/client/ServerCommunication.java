package nz.ac.aut.hss.client;

import com.sun.istack.internal.NotNull;

import java.security.interfaces.ECPublicKey;
import java.util.Map;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ServerCommunication {
	public void requestJoin() {

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
}
