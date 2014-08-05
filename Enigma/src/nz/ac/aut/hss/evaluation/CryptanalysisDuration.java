package nz.ac.aut.hss.evaluation;

import nz.ac.aut.hss.cryptanalysis.EnigmaAnalyzer;
import nz.ac.aut.hss.encrypt.Enigma;
import nz.ac.aut.hss.util.Stats;

import java.io.IOException;

/**
 * @author Martin Schrimpf
 * @created 04.08.2014
 */
public class CryptanalysisDuration {
	private final int loops;

	public CryptanalysisDuration(final int loops) {
		this.loops = loops;
	}

	private void run(final int rotorsMin, final int rotorsMax, final int rotorsStep) throws IOException {
		System.out.println("Rotors,Time (ms)");
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		final String key = "MAS";
		final String ciphertext = new Enigma().encrypt(plaintext, key);
		for (int rotors = rotorsMin; rotors <= rotorsMax; rotors += rotorsStep) {
			System.out.print(rotors + ",");
			final EnigmaAnalyzer analyzer = new EnigmaAnalyzer(rotors);
			final Stats stats = new Stats();
			for (int l = 0; l < loops; l++) {
				stats.startMeasurement();
				analyzer.findKey(ciphertext);
				stats.stopMeasurementAndAddValue();
			}
			System.out.println(stats.getExpectedValue());
		}
	}

	public static void main(String[] args) throws IOException {
		new CryptanalysisDuration(10).run(1, 5, 1);
	}
}
