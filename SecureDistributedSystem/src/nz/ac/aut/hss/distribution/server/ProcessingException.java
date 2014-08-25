package nz.ac.aut.hss.distribution.server;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ProcessingException extends Exception {
	public ProcessingException() {
		super();
	}

	public ProcessingException(final String message) {
		super(message);
	}

	public ProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ProcessingException(final Throwable cause) {
		super(cause);
	}

	public ProcessingException(final String message, final Throwable cause, final boolean enableSuppression,
							   final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
