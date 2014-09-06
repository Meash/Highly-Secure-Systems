package nz.ac.aut.hss.crypt;

import nz.ac.aut.hss.distribution.crypt.ECCEncryption;
import nz.ac.aut.hss.distribution.crypt.Encryption;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
@Ignore
public class ECCTest extends EncryptionTest {
	private static KeyPair keyPair;

	@BeforeClass
	public static void initKey() throws Exception {
		keyPair = ECCEncryption.createKeyPair();
	}

	@Override
	protected Encryption createEncryption() throws Exception {
		return new ECCEncryption((ECPrivateKey) keyPair.getPrivate(), (ECPublicKey) keyPair.getPublic());
	}
}
