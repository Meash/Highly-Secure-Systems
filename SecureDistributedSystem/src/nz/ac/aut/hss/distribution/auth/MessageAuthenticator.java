package nz.ac.aut.hss.distribution.auth;

import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.Hasher;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

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

	public boolean verify(Message msg, final ECPublicKey publicKey, final ECPrivateKey privateKey) throws IOException {
		if (msg.authentication == null)
			throw new IllegalArgumentException("Message does not contain authentication hash");
		final String authentication = msg.authentication;
		msg.authentication = null;
		return hash(msg, publicKey).equals(/* decrypt() */authentication);
	}

	public String hash(final Message msg, final ECPublicKey key) throws IOException {
		if (msg.authentication != null)
			throw new IllegalStateException("Message's authentication is set already");
		final String msgString = serializer.serialize(msg);
		final String hash = hasher.hash(msgString);
		return /* encrypt() */ hash; // TODO encrypt with public key
	}
}
