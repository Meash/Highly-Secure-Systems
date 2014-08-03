package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public interface Decrypter {
	public String decrypt(String ciphertext, String key);
}
