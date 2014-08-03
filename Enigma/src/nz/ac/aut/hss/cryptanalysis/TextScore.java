package nz.ac.aut.hss.cryptanalysis;

/**
 * @author Martin Schrimpf
 * @created 03.08.2014
 */
public interface TextScore {
	/**
	 * Calculates the score of the text.
	 * This method is not case-sensitive.
	 * @param text the text
	 * @return the score of the text according to the metric of this class
	 */
	public double valueOf(String text);
}
