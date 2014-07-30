package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public interface Encrypter {
	public String decrypt(String plaintext, String key);
}
