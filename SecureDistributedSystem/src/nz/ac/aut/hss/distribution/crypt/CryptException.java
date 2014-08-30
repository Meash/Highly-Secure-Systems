package nz.ac.aut.hss.distribution.crypt;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class CryptException extends Exception {
	public CryptException() {
	}

	public CryptException(final String message) {
		super(message);
	}

	public CryptException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CryptException(final Throwable cause) {
		super(cause);
	}

	public CryptException(final String message, final Throwable cause, final boolean enableSuppression,
						  final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
