package nz.ac.aut.hss.client.communication;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ClientDoesNotExistException extends Exception {
	public ClientDoesNotExistException() {
	}

	public ClientDoesNotExistException(final String message) {
		super(message);
	}

	public ClientDoesNotExistException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ClientDoesNotExistException(final Throwable cause) {
		super(cause);
	}
}
