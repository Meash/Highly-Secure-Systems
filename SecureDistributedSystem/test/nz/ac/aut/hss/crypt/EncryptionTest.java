package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public abstract class EncryptionTest {
	private Encryption encryption;

	@Before
	public void initEncryption() throws Exception {
		encryption = createEncryption();
	}

	protected abstract Encryption createEncryption() throws Exception;

	@Test
	public void reverse() throws CryptException {
		final String plain = "sdfjksn123 904#";
		final String cipher = encryption.encrypt(plain);
		Assert.assertEquals(plain, encryption.decrypt(cipher));
	}
}
