package nz.ac.aut.hss.encrypt;

/**
 * @see <a href="http://www.codesandciphers.org.uk/enigma/enigma3.htm">documentation</a>,
 * <a href="http://www.codesandciphers.org.uk/enigma/rotorspec.htm">rotor specification</a>,
 * <a href="http://www.codesandciphers.org.uk/enigma/example1.htm">example</a>
 */
public class Enigma implements Encrypter, Decrypter {
	public static final char[] ALPHABET =
			{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
					'V', 'W', 'X', 'Y', 'Z'};
	/**
	 * Follows the actual rotor orders.
	 * See http://www.codesandciphers.org.uk/enigma/rotorspec.htm
	 */
	public static final char[][] ALPHABET_SCRAMBLED = {
			// 1
			{'E', 'K', 'M', 'F', 'L', 'G', 'D', 'Q', 'V', 'Z', 'N', 'T', 'O', 'W', 'Y', 'H', 'X', 'U', 'S', 'P', 'A',
					'I', 'B', 'R', 'C', 'J'},
			// 2
			{'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P',
					'Y', 'F', 'V', 'O', 'E'},
			// 3
			{'B', 'D', 'F', 'H', 'J', 'L', 'C', 'P', 'R', 'T', 'X', 'V', 'Z', 'N', 'Y', 'E', 'I', 'W', 'G', 'A', 'K',
					'M', 'U', 'S', 'Q', 'O'},
			// 4
			{'E', 'S', 'O', 'V', 'P', 'Z', 'J', 'A', 'Y', 'Q', 'U', 'I', 'R', 'H', 'X', 'L', 'N', 'F', 'T', 'G', 'K',
					'D', 'C', 'M', 'W', 'B'},
			// 5
			{'V', 'Z', 'B', 'R', 'G', 'I', 'T', 'Y', 'U', 'P', 'S', 'D', 'N', 'H', 'L', 'X', 'A', 'W', 'M', 'J', 'Q',
					'O', 'F', 'E', 'C', 'K'},
			// 6
			{'J', 'P', 'G', 'V', 'O', 'U', 'M', 'F', 'Y', 'Q', 'B', 'E', 'N', 'H', 'Z', 'R', 'D', 'K', 'A', 'S', 'X',
					'L', 'I', 'C', 'T', 'W'},
			// 7
			{'N', 'Z', 'J', 'H', 'G', 'R', 'C', 'X', 'M', 'Y', 'S', 'W', 'B', 'O', 'U', 'F', 'A', 'I', 'V', 'L', 'P',
					'E', 'K', 'Q', 'D', 'T'},
			// 8
			{'F', 'K', 'Q', 'H', 'T', 'L', 'X', 'O', 'C', 'B', 'J', 'S', 'P', 'D', 'Z', 'R', 'A', 'M', 'E', 'W', 'N',
					'I', 'U', 'Y', 'G', 'V'},
	};

	/** Matrix of rotors x ALPHABET_SIZE */
	private char[][] rotors;

	/** Current index of each rotor */
	private final int[] rotorPositions;

	public Enigma(final int rotors) {
		this.rotorPositions = new int[rotors];
		this.rotors = new char[rotors][];
		System.arraycopy(ALPHABET_SCRAMBLED, 0, this.rotors, 0, rotors); // fill with pre-determined
	}

	/**
	 * Rotates the rotors to the initial position as indicated by the key.
	 * @param key the key consisting of upper-case alpha characters only
	 */
	private void applyRotations(final String key) {
		validateKey(key);
		for (int i = 0; i < rotorPositions.length; i++) {
			rotorPositions[i] = key.charAt(i) - 'A';
		}
	}

	public int getRotors() {
		return rotorPositions.length;
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
			int index = chars[c] - 'A';
			for (int i = from; from < to ? i < to : i >= to; i += direction) {
				index = index + direction * (rotors[i][rotorPositions[i]] - 'A');
			}
			index %= ALPHABET.length;
			if (index < 0)
				index += ALPHABET.length;
			chars[c] = ALPHABET[index];
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