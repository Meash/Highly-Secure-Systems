package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class RSA implements Encryption {
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	private static final String ALGORITHM = "RSA";
	private final Key key1, key2;

	public RSA(final Key key1, final Key key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	@Override
	public String encrypt(final String plain) throws CryptException {
		return convert(plain, Cipher.ENCRYPT_MODE);
	}

	private String convert(final String input, final int mode) throws CryptException {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			final Key key = mode == Cipher.ENCRYPT_MODE ? key1 : key2;
			if (key == null)
				throw new IllegalStateException(
						"Key for " + (mode == Cipher.ENCRYPT_MODE ? "encryption" : "decryption") + " not set");
			cipher.init(mode, key);
			byte[] bytes = input.getBytes(Encryption.CHARSET);
			byte[] cipherText = cipher.doFinal(bytes);
			return new String(cipherText, Encryption.CHARSET);
		} catch (InvalidKeyException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptException(e);
		}
	}

	@Override
	public String decrypt(final String cipher) throws CryptException {
		return convert(cipher, Cipher.DECRYPT_MODE);
	}

	public static KeyPair createKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
		kpg.initialize(4096); //2048);
		return kpg.generateKeyPair();
	}
}
