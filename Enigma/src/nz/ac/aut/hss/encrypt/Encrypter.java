package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public interface Encrypter {
	/**
	 * @param plaintext the unencrypted text in all lower-case
	 * @param key the key to use
	 * @return the ciphertext in all upper-case
	 */
	public String encrypt(String plaintext, String key);
}
