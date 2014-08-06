package nz.ac.aut.hss.encrypt;

public class ReflectorEnigmaTest extends AbstractEnigmaTest {
	@Override
	public Enigma createMachine(final int rotors) {
		return new ReflectorEnigma(3);
	}
}
