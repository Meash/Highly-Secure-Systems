package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import org.junit.BeforeClass;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class AESTest extends EncryptionTest {
	private static SecretKey key;

	@BeforeClass
	public static void initKey() throws NoSuchProviderException, NoSuchAlgorithmException {
		key = AES.createKey(128);
	}

	@Override
	protected Encryption createEncryption() throws Exception {
		return new AES(key);
	}
}
