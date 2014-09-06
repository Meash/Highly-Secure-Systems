package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.AES;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.crypt.KeyUtil;
import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class KeyUtilTest {
	@Test
	public void aes() throws Exception {
		final KeyUtil keyUtil = new KeyUtil("AES");
		final SecretKey key = AES.createKey(128);
		final Encryption encrypter = new AES(key);

		final String password = keyUtil.toString(key);
		final SecretKey restoredKey = keyUtil.toKey(password);
		final AES decrypter = new AES(restoredKey);

		final String plain = "some test string 123!";
		final String cipher = encrypter.encrypt(plain);
		assertEquals(plain, decrypter.decrypt(cipher));
	}

	@Test
	public void repeatedAes() throws Exception {
		for (int i = 0; i < 10; i++) {
			aes();
		}
	}
}
