package nz.ac.aut.hss.cryptanalysis;

/**
 * @author Martin Schrimpf
 * @created 31.07.2014
 */
public interface CryptAnalyzer {
	public String findKey(String... cipherTexts);
}
