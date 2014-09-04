package nz.ac.aut.hss.distribution.crypt;

import com.sun.istack.internal.Nullable;
import nz.ac.aut.hss.distribution.protocol.ClientInformationMessage;
import nz.ac.aut.hss.distribution.protocol.EncryptedMessage;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.server.KeyAuthority;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class ServerMessageEncrypter extends MessageEncrypter {
	@Nullable
	private final KeyAuthority authority;

	public ServerMessageEncrypter(final KeyAuthority authority) {
		this.authority = authority;
	}

	public Message decrypt(final Message input, final String clientId)
			throws IOException, CryptException, ClassNotFoundException {
		if (!(input instanceof EncryptedMessage))
			return input;

		final Encryption[] encryptions;
		switch (input.identifier) {
			case ClientInformationMessage.IDENTIFIER:
				final SecretKey oneTimePass = authority.getOneTimePass(clientId);
				if (oneTimePass == null)
					throw new IllegalStateException(
							"One time password for client '" + clientId + "' has not been set, no previous request?");
				try {
					encryptions = new Encryption[]{new AES(oneTimePass), new Base64Encryption()};
				} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
					throw new CryptException(e);
				}
				break;
			default:
				throw new IllegalArgumentException("Unsupported identifier '" + input.identifier + "'");
		}
		return decrypt((EncryptedMessage) input, encryptions);
	}
}
