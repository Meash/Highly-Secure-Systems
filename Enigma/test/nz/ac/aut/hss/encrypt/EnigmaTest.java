package nz.ac.aut.hss.encrypt;

public class EnigmaTest extends AbstractEnigmaTest {
	@Override
	public Enigma createMachine(final int rotors) {
		return new Enigma(3);
	}
}
