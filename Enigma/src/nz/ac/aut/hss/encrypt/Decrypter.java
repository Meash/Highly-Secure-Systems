package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public interface Decrypter {
	/**
	 * @param ciphertext the ciphertext in upper-case
	 * @param key the key used to encrypt the plaintext
	 * @return the plaintext in all lower-case derived from the ciphertext and the key
	 */
	public String decrypt(String ciphertext, String key);
	
	/**
         * Decrypts cipher text which was encrypted using reflector.
	 * @param ciphertext the ciphertext in upper-case
	 * @param key the key used to encrypt the plaintext
	 * @return the plaintext in all lower-case derived from the ciphertext and the key
	 */
    public String decryptWithReflector(String cipherText, String key);
}
