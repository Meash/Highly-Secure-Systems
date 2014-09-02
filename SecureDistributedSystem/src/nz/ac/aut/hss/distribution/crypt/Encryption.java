package nz.ac.aut.hss.distribution.crypt;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public abstract class Encryption {
	public static final String CHARSET = "ISO-8859-1";

	public abstract String encrypt(final String plain) throws CryptException;

	public abstract String decrypt(final String cipher) throws CryptException;
}
