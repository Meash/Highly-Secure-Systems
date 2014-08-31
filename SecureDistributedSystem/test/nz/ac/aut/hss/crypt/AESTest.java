package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.CryptException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 31.08.2014
 */
public class AESTest {
	private static SecretKey key;
	private AES aes;

	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchProviderException, NoSuchAlgorithmException {
		key = AES.createKey(128);
	}

	@Before
	public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException {
		aes = new AES(key);
	}

	@Test
	public void reverse() throws CryptException {
		final String plain = "sdfjksn123 904#";
		final String cipher = aes.encrypt(plain);
		assertEquals(plain, aes.decrypt(cipher));
	}
}
