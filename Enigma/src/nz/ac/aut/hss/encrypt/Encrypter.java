package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 * @created 30.07.2014
 */
public interface Encrypter {
	public String encrypt(String plaintext, String key);
}
