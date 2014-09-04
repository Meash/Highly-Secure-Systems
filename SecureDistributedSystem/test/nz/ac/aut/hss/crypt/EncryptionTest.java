package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

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
	public void specialCharsReverse() throws CryptException {
		final String plain =
				"\\=S\u007Føö'\u000FñP·vì Ç7Ç[\u000E2ð1ÜkÌÚý>\u0081K\u0088X÷¶M\u0085.\u009DWQk:\u008BÎ\u008Eb\b\u009BvÐß¤\u0088±­<£ý¾3G\u0083Ë\u009Ch/\u0015v\u000E\u009D\u001FÐ¶£â\u0006|£c\u009DüTö4\u009D\u0099¬;\u0012\u009BCTô'(xE÷ä|Ãb\u0094N\u008E§Ò@¦\u0081çBñtHï^µû¬N\u0010E±\u001BX\u008Aw";
		testReverse(plain);
	}

	@Test
	public void alphaNumericReverse() throws CryptException {
		final String plain = "sdfjksn123 904";
		testReverse(plain);
	}

	private void testReverse(final String plain) throws CryptException {
		final String cipher = encryption.encrypt(plain);
		Assert.assertEquals(plain, encryption.decrypt(cipher));
	}

	@Test
	public void diff() throws CryptException {
		final String cipher = encryption.encrypt("hello");
		assertNotEquals("wello", encryption.decrypt(cipher));
	}
}
