package nz.ac.aut.hss.encrypt;

/**
 * @author Martin Schrimpf
 */
public class Enigma implements Encrypter, Decrypter {
	public static final char[] ALPHABET =
			{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
					'V', 'W', 'X', 'Y', 'Z'};

	/** Current index of each rotor */
	private final int[] rotorPositions;

	public Enigma(final int rotors) {
		this.rotorPositions = new int[rotors];
	}

	/**
	 * Rotates the rotors to the initial position as indicated by the key.
	 * @param key the key consisting of upper-case alpha characters only
	 */
	private void applyRotations(final String key) {
		validateKey(key);
		for (int i = 0; i < rotorPositions.length; i++) {
			rotorPositions[i] = key.charAt(i) - ALPHABET[0];
		}
	}

	private enum Mode {
		ENCODE, DECODE
	}

	private String encode(String input, Mode mode) {
		char[] chars = input.toCharArray();
		// run either from inner to outer rotor (encryption) or vice-versa (decryption)
		int from = -1, to = -1, direction = 0;
		switch (mode) {
			case ENCODE:
				from = 0;
				to = rotorPositions.length;
				direction = 1;
				break;
			case DECODE:
				from = rotorPositions.length - 1;
				to = 0;
				direction = -1;
				break;
		}
		for (int c = 0; c < chars.length; c++) {
			int index = chars[c] - ALPHABET[0];
			for (int i = from; from < to ? i < to : i >= to; i += direction) {
				index = index + direction * rotorPositions[i];
			}
			chars[c] = (char) (index + ALPHABET[0]);
			rotorTick();
		}
		return new String(chars);
	}

	private void rotorTick() {
		rotorPositions[0]++; // always move first rotor
		for (int i = 0; i < rotorPositions.length; i++) {
			if (rotorPositions[i] == ALPHABET.length) { // full rotation reached
				rotorPositions[i] = 0; // reset
				if (i < rotorPositions.length - 1) { // move next rotor if existent
					rotorPositions[i + 1]++;
				}
			}
		}
	}

	@Override
	public String encrypt(final String plaintext, final String key) {
		applyRotations(key);
		return encode(plaintext.toUpperCase(), Mode.ENCODE);
	}

	@Override
	public String decrypt(final String ciphertext, final String key) {
		applyRotations(key);
		return encode(ciphertext, Mode.DECODE).toLowerCase();
	}

	private void validateKey(final String key) {
		// length
		if (key.length() != rotorPositions.length)
			throw new IllegalArgumentException("Key length (" + key.length() + ") must be equal " +
					"to the number of rotors (" + rotorPositions.length + ")");
		// characters
		for (char c : key.toCharArray()) {
			boolean found = false;
			for (char a : ALPHABET) {
				if (c == a) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IllegalArgumentException("Key contains the invalid character " + c);
			}
		}
	}
}