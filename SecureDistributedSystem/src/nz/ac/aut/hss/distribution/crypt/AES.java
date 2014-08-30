package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class AES extends Encryption {
	private static final String ENCRYPTION = "AES/CBC/PKCS5Padding";
	private final SecretKey secret;

	public AES(final SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException {
		this.secret = secret;
	}

	public String encrypt(final String plaintext) throws CryptException {
		try {
			Cipher encryptCipher = Cipher.getInstance(ENCRYPTION);
			encryptCipher.init(Cipher.ENCRYPT_MODE, secret);
			byte[] ciphertext = encryptCipher.doFinal(plaintext.getBytes("UTF-8"));
			return new String(ciphertext);
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptException(e);
		}
	}

	@Override
	public String decrypt(final String cipher) throws CryptException {
		try {
			final Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, secret);
			final byte[] bytes = cipher.getBytes("UTF-8");
			return new String(decryptCipher.doFinal(bytes), "UTF-8");
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptException(e);
		}
	}
}
