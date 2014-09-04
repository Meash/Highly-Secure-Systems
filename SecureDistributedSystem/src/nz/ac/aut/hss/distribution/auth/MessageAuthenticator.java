package nz.ac.aut.hss.distribution.auth;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.Hasher;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Serializes the messages and checks their hash.
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class MessageAuthenticator {
	private final ObjectSerializer serializer;
	private final Hasher hasher;

	public MessageAuthenticator() throws NoSuchAlgorithmException {
		serializer = new ObjectSerializer();
		hasher = new Hasher();
	}

	public boolean verify(Message msg, final PublicKey publicKey) throws IOException, CryptException {
		if (msg.authentication == null)
			throw new IllegalArgumentException("Message does not contain authentication hash");
		final String encryptedHash = msg.authentication;
		msg.authentication = null; // reset to state without authentication
		final Encryption encryption = new RSA(null, publicKey);
		final String decryptedHash;
		try {
			decryptedHash = encryption.decrypt(encryptedHash);
		} catch (CryptException ex) {
			return false; // could not decrypt due to invalid authentication (e.g. padding)
		}
		final String actualHash = hash(msg);
		return actualHash.equals(decryptedHash);
	}

	private String hash(final Message msg) throws IOException {
		if (msg.authentication != null)
			throw new IllegalStateException("Message's authentication is set already");
		final String msgString = serializer.serialize(msg);
		return hasher.hash(msgString);
	}

	public String hash(final Message msg, final PrivateKey privateKey) throws IOException, CryptException {
		final String hash = hash(msg);
		final Encryption encryption = new RSA(privateKey, null);
		return encryption.encrypt(hash);
	}
}
