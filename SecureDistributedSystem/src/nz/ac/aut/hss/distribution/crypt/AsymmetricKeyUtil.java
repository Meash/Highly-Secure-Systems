package nz.ac.aut.hss.distribution.crypt;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class AsymmetricKeyUtil {
	private final String algorithm;

	public AsymmetricKeyUtil(final String algorithm) {
		this.algorithm = algorithm;
	}

	public String toString(Key key) throws UnsupportedEncodingException {
		byte[] bytes = key.getEncoded();
		return new String(bytes, Encryption.CHARSET);
	}

	public PrivateKey toPrivateKey(String str)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] bytes = str.getBytes(Encryption.CHARSET);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
		return keyFactory.generatePrivate(privateKeySpec);
	}

	public PublicKey toPublicKey(String str)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] bytes = str.getBytes(Encryption.CHARSET);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
		return keyFactory.generatePublic(publicKeySpec);
	}
}
