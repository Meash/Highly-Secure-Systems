package nz.ac.aut.hss.evaluation;

import nz.ac.aut.hss.cryptanalysis.EnigmaAnalyzer;
import nz.ac.aut.hss.encrypt.Enigma;
import nz.ac.aut.hss.encrypt.ReflectorEnigma;
import nz.ac.aut.hss.util.KeyUtils;
import nz.ac.aut.hss.util.Stats;

import java.io.IOException;

public class CryptanalysisDuration {
	private final int loops;

	public CryptanalysisDuration(final int loops) {
		this.loops = loops;
	}

	private void run(final int rotorsMin, final int rotorsMax, final int rotorsStep) throws IOException {
		System.out.println("Rotors,Time (ms)");
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
//		final String plaintext =
//				"Loremipsumdolorsitametconsetetursadipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreet" +
//						"doloremagnaaliquyameratseddiamvoluptuaAtveroeosetaccusametjustoduodoloresetearebum" +
//						"StetclitakasdgubergrennoseatakimatasanctusestLoremipsumdolorsitametLoremipsumdolorsitamet" +
//						"consetetursadipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreetdoloremagnaali" +
//						"quyameratseddiamvoluptuaAtveroeosetaccusametjustoduodoloresetearebumStetclitakasd" +
//						"gubergrennoseatakimatasanctusestLoremipsumdolorsitametLoremipsumdolorsitametconsetetursa" +
//						"dipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreetdoloremagnaaliquyameratseddi" +
//						"amvoluptuaAtveroeosetaccusametjustoduodoloresetearebumStetclitakasdgubergrennoseatakima" +
//						"tasanctusestLoremipsumdolorsitametDuisautemveleumiriuredolorinhendreritinvulputate" +
//						"velitessemolestieconsequatvelillumdoloreeufeugiatnullafacilisisatveroerosetaccumsane" +
//						"tiustoodiodignissimquiblanditpraesentluptatumzzrildelenitaugueduisdoloretefeugait" +
//						"nullafacilisiLoremipsumdolorsitametconsectetueradipiscingelitseddiamnonummynibheuismod" +
//						"tinciduntutlaoreetdoloremagnaaliquameratvolutpatUtwisienimadminimveniamquisnostru" +
//						"dexercitationullamcorpersuscipitlobortisnislutaliquipexeacommodoconsequatDuisautem" +
//						"veleumiriuredolorinhendreritinvulputatevelitesse";
		for (int rotors = rotorsMin; rotors <= rotorsMax; rotors += rotorsStep) {
			System.out.print(rotors + ",");
			final String key = KeyUtils.randomKey(rotors, Enigma.ALPHABET);
			final Enigma enigma = new ReflectorEnigma(rotors);
			final String ciphertext = enigma.encrypt(plaintext, key);
			final EnigmaAnalyzer analyzer = new EnigmaAnalyzer(enigma);
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
		new CryptanalysisDuration(3).run(1, 5, 1);
	}
}
