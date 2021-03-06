package nz.ac.aut.hss.cryptanalysis;

import nz.ac.aut.hss.encrypt.Enigma;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnigmaAnalyzerTest {
	private static EnigmaAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.print("Setting up...");
		System.out.flush();
		analyzer = new EnigmaAnalyzer(new Enigma(3));
		System.out.println(" complete.");
	}

	@Test
	public void shortCipherText() throws Exception {
		final String plaintext = "intelligencepointstoattackontheeastwallofthecastleatdawn";
		test(plaintext, "MAS");
	}

	@Test
	public void longCipherText() throws Exception {
		final String plaintext =
				"Loremipsumdolorsitametconsetetursadipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreet" +
						"doloremagnaaliquyameratseddiamvoluptuaAtveroeosetaccusametjustoduodoloresetearebum" +
						"StetclitakasdgubergrennoseatakimatasanctusestLoremipsumdolorsitametLoremipsumdolorsitamet" +
						"consetetursadipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreetdoloremagnaali" +
						"quyameratseddiamvoluptuaAtveroeosetaccusametjustoduodoloresetearebumStetclitakasd" +
						"gubergrennoseatakimatasanctusestLoremipsumdolorsitametLoremipsumdolorsitametconsetetursa" +
						"dipscingelitrseddiamnonumyeirmodtemporinviduntutlaboreetdoloremagnaaliquyameratseddi" +
						"amvoluptuaAtveroeosetaccusametjustoduodoloresetearebumStetclitakasdgubergrennoseatakima" +
						"tasanctusestLoremipsumdolorsitametDuisautemveleumiriuredolorinhendreritinvulputate" +
						"velitessemolestieconsequatvelillumdoloreeufeugiatnullafacilisisatveroerosetaccumsane" +
						"tiustoodiodignissimquiblanditpraesentluptatumzzrildelenitaugueduisdoloretefeugait" +
						"nullafacilisiLoremipsumdolorsitametconsectetueradipiscingelitseddiamnonummynibheuismod" +
						"tinciduntutlaoreetdoloremagnaaliquameratvolutpatUtwisienimadminimveniamquisnostru" +
						"dexercitationullamcorpersuscipitlobortisnislutaliquipexeacommodoconsequatDuisautem" +
						"veleumiriuredolorinhendreritinvulputatevelitesse";
		test(plaintext, "MAS");
	}

	@Test
	public void greeting() throws Exception {
		final String plaintext = "togeneraloberzalekxnothingtoreport";
		test(plaintext, "MAS");
	}

	private void test(final String plaintext, final String key) {
		final Enigma enigma = new Enigma(3);
		final String ciphertext = enigma.encrypt(plaintext, key);
		System.out.printf("%15s: %s\n", "Cipher", ciphertext);
		final String analyzedKey = analyzer.findKey(ciphertext);
		System.out.printf("%15s: %s\n", "Analyzed Key", analyzedKey);
		final String analyzedPlaintext = enigma.decrypt(ciphertext, analyzedKey);
		System.out.printf("%15s: %s\n", "Analyzed Plain", analyzedPlaintext);
		assertEquals(plaintext, analyzedPlaintext);
		assertEquals(key, analyzedKey); // TODO: move below print key
	}
}
