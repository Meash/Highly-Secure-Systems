package nz.ac.aut.hss.encrypt;

/**
 * En-/decrypts cipher text using a reflector.
 */
public class ReflectorEnigma extends Enigma {
	private static final char[] REFLECTOR =
			{'Y', 'R', 'U', 'H', 'Q', 'S', 'L', 'D', 'P', 'X', 'N', 'G', 'O', 'K', 'M', 'I', 'E', 'B', 'F', 'Z', 'C',
					'W', 'V', 'J', 'A', 'T'};

	public ReflectorEnigma(final int rotors) {
		super(rotors);
	}

	/**
	 * Encrypt a text using reflector. Also used for decrypting.
	 * If 3 rotor was used, this algorithm will use total of 7 rotors.
	 * 3 rotor + reflector + 3 rotor backwards.
	 * @param input the plain- or ciphertext
	 * @return encrypted or decrypted text
	 */
	private String convertWithReflector(String input) {
		char[] chars = input.toCharArray();
		for (int c = 0; c < chars.length; c++) {
			int index = chars[c] - 'A';
			//Forwards
			for(int i = 0; i < rotorPositions.length;i++){
				index = index + (rotors[i][rotorPositions[i]] - 'A');
			}
			index %= ALPHABET.length;
			//Reflector
			index = REFLECTOR[index] - 'A';
			//Backwards
			for(int i = rotorPositions.length-1; 0 <= i;i--){
				index = index - (rotors[i][rotorPositions[i]] - 'A');
			}
			index %= ALPHABET.length;
			if (index < 0)
				index += ALPHABET.length;
			//Done
			chars[c] = ALPHABET[index];
			rotorTick();
		}
		return new String(chars);
	}

	@Override
	public String encrypt(String plaintext, String Key) {
		applyRotations(Key);
		return convertWithReflector(plaintext.toUpperCase());
	}

	@Override
	public String decrypt(String cipherText, String key) {
		applyRotations(key);
		return convertWithReflector(cipherText.toUpperCase()).toLowerCase();
	}
}
