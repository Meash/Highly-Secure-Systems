package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class AES extends Encryption {
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final IvParameterSpec initVector =
			new IvParameterSpec(new byte[]{1, -2, 3, -4, 5, -6, 7, -8, 9, -10, 11, -12, 13, -14, 15, -16});
	private final SecretKey secret;

	public AES(final SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException {
		this.secret = secret;
	}

	public String encrypt(final String plaintext) throws CryptException {
		try {
			final Cipher encryptCipher = initCipher(Cipher.ENCRYPT_MODE);
			return transform(encryptCipher, plaintext);
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptException(e);
		}
	}

	@Override
	public String decrypt(final String ciphertext) throws CryptException {
		try {
			final Cipher decryptCipher = initCipher(Cipher.DECRYPT_MODE);
			return transform(decryptCipher, ciphertext);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			throw new CryptException(e);
		}
	}

	private Cipher initCipher(final int mode)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		final Cipher encryptCipher = Cipher.getInstance(TRANSFORMATION);
		encryptCipher.init(mode, secret, initVector);
		return encryptCipher;
	}

	private String transform(final Cipher cipher, final String str)
			throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
		final byte[] bytes = str.getBytes(CHARSET);
		final byte[] result = cipher.doFinal(bytes);
		return new String(result, CHARSET);
	}

	public static SecretKey createKey(final int keySize) throws NoSuchProviderException, NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(keySize);
		return keyGen.generateKey();
	}
}
