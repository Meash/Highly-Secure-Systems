package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.CryptException;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

	@Ignore
	@Test
	public void bigString() throws Exception {
		final String plain =
				"rO0ABXNyAD9uei5hYy5hdXQuaHNzLmRpc3RyaWJ1dGlvbi5wcm90b2NvbC5Db21tdW5pY2F0aW9uUmVxdWVzdE1lc3NhZ2X7SSvr902NqAIAA0wABW5vbmNldAASTGphdmEvbGFuZy9TdHJpbmc7TAAJcHVibGljS2V5dAAZTGphdmEvc2VjdXJpdHkvUHVibGljS2V5O0wACnNlc3Npb25LZXl0ABhMamF2YXgvY3J5cHRvL1NlY3JldEtleTt4cgArbnouYWMuYXV0Lmhzcy5kaXN0cmlidXRpb24ucHJvdG9jb2wuTWVzc2FnZQ7dSI6fSHF9AgADTAAOYXV0aGVudGljYXRpb25xAH4AAVsAC2VuY3J5cHRpb25zdAAuW0xuei9hYy9hdXQvaHNzL2Rpc3RyaWJ1dGlvbi9jcnlwdC9FbmNyeXB0aW9uO0wACmlkZW50aWZpZXJxAH4AAXhwcHVyAC5bTG56LmFjLmF1dC5oc3MuZGlzdHJpYnV0aW9uLmNyeXB0LkVuY3J5cHRpb247AtGFWYD9KyQCAAB4cAAAAAB0ABNjbGllbnRfbGlzdF9yZXF1ZXN0dAADNDU2c3IAFGphdmEuc2VjdXJpdHkuS2V5UmVwvflPs4iapUMCAARMAAlhbGdvcml0aG1xAH4AAVsAB2VuY29kZWR0AAJbQkwABmZvcm1hdHEAfgABTAAEdHlwZXQAG0xqYXZhL3NlY3VyaXR5L0tleVJlcCRUeXBlO3hwdAADUlNBdXIAAltCrPMX+AYIVOACAAB4cAAAASYwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCWqBYjLORqZAMpFsm/mJUp0fYjMXbqNZGbKLvO+flDL2uz+3q1MMhvkMWDAMrsphc/cQehG8kxKdAdIYASV6NDoKQL1XLBAxtHfqMJYWL7GjMSPbk7yFOBBfiMbeUtqlaq2pQk5iyaB8c1ABD1uTb34K1wBJPd5pB2J5g8O3iY/3QTZRoHnoXY16o9lBRf0huJGrts14igfuKS+adVGNOx4MN1n7XObLQUhklQ0IKD9SId8GsoaoAgILjuOh0yJ6M9RQskeOiACLQkBV7fmMmHR8EkRK5TB7S1PRnVBCB9BTuYpVg/hy1odb5CKSX7yoJXc5X3jeRD2YQ+0oYEI+SVAgMBAAF0AAVYLjUwOX5yABlqYXZhLnNlY3VyaXR5LktleVJlcCRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAGUFVCTElDc3IAH2phdmF4LmNyeXB0by5zcGVjLlNlY3JldEtleVNwZWNbRwtm4jBhTQIAAkwACWFsZ29yaXRobXEAfgABWwADa2V5cQB+AAx4cHQAA0FFU3VxAH4AEAAAABB5wSgYrvxnyGqYE3pIS08u";
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
