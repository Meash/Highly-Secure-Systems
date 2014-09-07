package nz.ac.aut.hss.distribution.util;

import nz.ac.aut.hss.distribution.crypt.Encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class Hasher {
	private final MessageDigest digest;

	public Hasher() throws NoSuchAlgorithmException {
		this.digest = MessageDigest.getInstance("SHA-256");
	}

	public String hash(final String str) throws UnsupportedEncodingException {
		byte[] hash = digest.digest(str.getBytes(Encryption.CHARSET));
		return new String(hash, Encryption.CHARSET);
	}

	public boolean matchesHash(final String source, final String hash) throws UnsupportedEncodingException {
		return hash(source).equals(hash);
	}
}
