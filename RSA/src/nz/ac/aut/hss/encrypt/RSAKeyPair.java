package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAKeyPair {
	public final RSAKey privateKey, publicKey;

	public RSAKeyPair(final RSAKey privateKey, final RSAKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
}
