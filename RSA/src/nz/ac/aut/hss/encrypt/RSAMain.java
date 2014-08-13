package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAMain {
	public static void main(String[] args) {
		new RSAMain().run();
	}

	private void run() {
		final RSA rsa = new RSA();
		final RSAKeyPair keys = RSAUtil.generateKeyPair();
		final String plaintext = "Hi IAmA test";
		final String ciphertext = rsa.convert(plaintext, keys.publicKey);
		System.out.println("Ciphertext: " + ciphertext);
		System.out.println("Reconstructed plain: " + rsa.convert(ciphertext, keys.privateKey));
	}
}
