package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.Encryption;
import nz.ac.aut.hss.distribution.crypt.RSA;
import org.junit.BeforeClass;

import java.security.KeyPair;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class RSATest extends EncryptionTest {
	private static KeyPair keyPair;

	@BeforeClass
	public static void initKey() throws Exception {
		keyPair = RSA.createKeyPair();
	}

	@Override
	protected Encryption createEncryption() throws Exception {
		return new RSA(keyPair.getPrivate(), keyPair.getPublic());
	}
}
