package nz.ac.aut.hss.cryptanalysis;

/**
 * @author Martin Schrimpf
 * @created 03.08.2014
 */
public class BestKeyStore {
	private String key;
	private double value = Double.MIN_VALUE;

	public void updateIfBetter(String key, double value) {
		if (value > this.value) {
			this.key = key;
			this.value = value;
		}
	}

	public String getBestKey() {
		return key;
	}

	public double getBestValue() {
		return value;
	}
}
