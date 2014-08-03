package nz.ac.aut.hss.encrypt;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Takuma Sato
 */
public class Enigma implements Encrypter, Decrypter {
	public static final char[] ALPHABET =
			{'B', 'D', 'F', 'H', 'J', 'L', 'N', 'P', 'R', 'T', 'V', 'X', 'Z', 'A', 'C', 'E', 'G', 'I', 'K', 'M',
					'O', 'Q', 'S', 'U', 'W', 'Y'};

	//Number of routers
	private final int NUMBER_OF_ROTORS = 3; // TODO either implement it or leave it out, so far this variable is useless
	//Bit lazy
	private static Scanner keyboard = new Scanner(System.in);

	// Values for three rotors
	private char[] innerRotor =
			{'G', 'N', 'U', 'A', 'H', 'O', 'V', 'B', 'I', 'P', 'W', 'C', 'J', 'Q', 'X', 'D', 'K', 'R', 'Y', 'E',
					'L', 'S', 'Z', 'F', 'M', 'T'};
	private char[] middleRotor =
			{'E', 'J', 'O', 'T', 'Y', 'C', 'H', 'M', 'R', 'W', 'A', 'F', 'K', 'P', 'U', 'Z', 'D', 'I', 'N', 'S',
					'X', 'B', 'G', 'L', 'Q', 'V'};
	private char[] outerRotor =
			{'B', 'D', 'F', 'H', 'J', 'L', 'N', 'P', 'R', 'T', 'V', 'X', 'Z', 'A', 'C', 'E', 'G', 'I', 'K', 'M',
					'O', 'Q', 'S', 'U', 'W', 'Y'};

	// Create index arrays corresponding to each of the above, so that a letter can be used directly
	// as an index and doesn't require searching.  In other words, rather than having to search within
	// the innerRotor array to find 'A' at index position 4, we have a innerRotorIndex array that has
	// an entry for each ASCII character, and at position 'A' (which is 65) we will store the value 4.
	// This will speed up the program, since we won't need to search through the array for each letter
	// for each lookup, but rather can go directly there using the letter as the index.
	private int[] innerRotorIndex = new int[128];
	private int[] middleRotorIndex = new int[128];
	private int[] outerRotorIndex = new int[128];

	// Starting rotor position is chosen by the user, and modifies the starting rotor values
	private char innerRotorStartingLetter = ' ';
	private char middleRotorStartingLetter = ' ';
	private char outerRotorStartingLetter = ' ';


	public static void main(String[] args) throws IOException {
		Enigma enigmaInstance = new Enigma();
		enigmaInstance.promptStartingRotorValues();
		System.out.print("Enter the text to be encoded: ");
		String plainText = keyboard.nextLine();
		char[] encodedText = enigmaInstance.encodeText(plainText);
		System.out.println("Encode complete, result: " + String.valueOf(encodedText));
		char[] decodedText = enigmaInstance.decodeText(encodedText);
		System.out.println("Decode complete, result: " + String.valueOf(decodedText));
	}

	/**
	 * Prompt for and store the starting rotor values.
	 */
	public void promptStartingRotorValues() {
		System.out
				.print("Enter the inner, mid, and outer rotor starting letters (e.g. ABC) or press enter for default: ");
		String userInput = keyboard.nextLine();

		if (userInput.length() == 0) {
			System.out.println("Rotor set to defualt starting position.");
			innerRotorStartingLetter = innerRotor[0];
			middleRotorStartingLetter = middleRotor[0];
			outerRotorStartingLetter = outerRotor[0];
		} else {
			userInput = userInput.toUpperCase();
			// extract characters for starting rotor values
			innerRotorStartingLetter = userInput.charAt(0);
			middleRotorStartingLetter = userInput.charAt(1);
			outerRotorStartingLetter = userInput.charAt(2);

			// Check input validity
			if ((userInput.length() != 3) ||
					(innerRotorStartingLetter != ' ' && !Character.isLetter(innerRotorStartingLetter)) ||
					(middleRotorStartingLetter != ' ' && !Character.isLetter(middleRotorStartingLetter)) ||
					(outerRotorStartingLetter != ' ' && !Character.isLetter(outerRotorStartingLetter))
					) {
				// if there is a problem, exit program
				System.out.println("Oops, could not set the rotor letters.");
				System.exit(-1);
			}
		}
		// Rotate rotor arrays to reflect the user-selected starting letter
		rotateRotors(innerRotorStartingLetter, middleRotorStartingLetter, outerRotorStartingLetter);

	}

	/**
	 * Rotate rotors based on starting letters.  Then reinitialize the arrays of rotor index values
	 * @param innerRotorStartingLetter
	 * @param middleRotorStartingLetter
	 * @param outerRotorStartingLetter
	 */
	private void rotateRotors(
			char innerRotorStartingLetter,
			char middleRotorStartingLetter,
			char outerRotorStartingLetter) {
		rotateRotorToStartingCharacter(innerRotorStartingLetter, innerRotor);
		rotateRotorToStartingCharacter(middleRotorStartingLetter, middleRotor);
		rotateRotorToStartingCharacter(outerRotorStartingLetter, outerRotor);

		setRotorIndexValues(innerRotorIndex, innerRotor);
		setRotorIndexValues(middleRotorIndex, middleRotor);
		setRotorIndexValues(outerRotorIndex, outerRotor);
	}


	/**
	 * rotate the rotor letters to reflect the user's choice of rotor starting position.
	 * @param theLetter
	 * @param theRotor
	 */
	private void rotateRotorToStartingCharacter(char theLetter, char[] theRotor) {
		//Make copy
		char[] theRotorCopy = new char[theRotor.length];
		System.arraycopy(theRotor, 0, theRotorCopy, 0, theRotor.length);

		int letterPosition = -1;
		// find the letter in the rotor
		boolean found = false;
		for (int i = 0; i < theRotorCopy.length; i++) {
			if (theRotor[i] == theLetter) {
				letterPosition = i;
				break;
			}
		}
		// Copy over the letters from the copy into the original
		// Start the copy at the letter which will now be at the beginning, and wrap around
		// back to the beginning of the array while copying.
		for (int i = 0; i < theRotorCopy.length; i++) {
			theRotor[i] = theRotorCopy[(i + letterPosition) % theRotor.length];
		}
	}


	/**
	 * Create index arrays corresponding to each of the above, so that a letter can be used directly
	 * as an index and doesn't require searching.
	 * E.g. 'A' = 65
	 * @param rotorIndex
	 * @param theRotorCharacters
	 */
	private void setRotorIndexValues(int[] rotorIndex, char[] theRotorCharacters) {
		// set default values to -1 for index values incase of invalid character.
		for (int i = 0; i < rotorIndex.length; i++) {
			rotorIndex[i] = -1;
		}

		for (int i = 0; i < theRotorCharacters.length; i++) {
			rotorIndex[theRotorCharacters[i]] = i;    // store the index where this character is found
		}

	}

	/**
	 * Encodes the given plaintext.
	 * Lower- and upper-case are not distinguished, all characters are converted to upper case
	 * @param plaintext the plaintext to encode
	 * @return the ciphertext
	 */
	public char[] encodeText(String plaintext) {
		plaintext = plaintext.toUpperCase();
		// convert to array of char
		char[] plainTextArray = plaintext.toCharArray();

		int plainTextCharValue;
		int innerRotorCharIndex;
		int middleRotorCharIndex;
		char outerRotorCharacter;
		char[] encodedText = new char[plaintext.length()];
		// Encrypt a letter in each loop
		for (int i = 0; i < plainTextArray.length; i++) {
			plainTextCharValue = (int) plainTextArray[i];
			// find the position where that character is on the inner rotor
			innerRotorCharIndex = innerRotorIndex[plainTextCharValue];
			// find the corresponding character at that position on the outer rotor. Add an extra
			// increment to reflect the inner rotor rotation as we go
			outerRotorCharacter = outerRotor[(innerRotorCharIndex + (i % ALPHABET.length)) % ALPHABET.length];
			// Find that character on the middle rotor
			middleRotorCharIndex = middleRotorIndex[(int) outerRotorCharacter];
			// Find the character on the outer rotor that matches that middle rotor position.
			// Each time the inner rotor makes a complete revolution, the middle rotor advances once.  Each of
			// these middle rotor rotations means the middle rotor then corresponds to one character *after* it
			// would otherwise on the outer rotor, so add this amount.
			outerRotorCharacter = outerRotor[(middleRotorCharIndex + (i / ALPHABET.length) % ALPHABET.length) % ALPHABET.length];

			encodedText[i] = outerRotorCharacter;
		}
		return encodedText;
	}


	public char[] decodeText(char[] cipherText) {
		int cipherTextCharValue;
		int innerRotorCharIndex;
		char innerRotorCharacter;
		char middleRotorCharacter;
		int outerRotorCharIndex;
		int numberOfRotorShifts;    // stores intermediate values in calculating offsets
		int incrementIndex;            // amount we have to add (using mod) to implement subtraction

		// Go through array of characters, decrypting each one as we go.
		for (int i = 0; i < cipherText.length; i++) {
			// get the character from the plainText array
			cipherTextCharValue = (int) cipherText[i];
			// find the position where that character is on the outer rotor
			outerRotorCharIndex = outerRotorIndex[cipherTextCharValue];

			// Store the middle rotor character in that same index position. Subtract from the index
			// the number of times the middle rotor has been rotated. Since we can't subtract and "wrap
			// around" in an array, instead we do the equivalent by adding (ALPHABET.length (26) - the_amount_to_subtract)
			numberOfRotorShifts = (i / ALPHABET.length) % ALPHABET.length;    // amount to be subtracted
			incrementIndex = ALPHABET.length - numberOfRotorShifts;    // turn subtraction into addition
			middleRotorCharacter = middleRotor[(outerRotorCharIndex + incrementIndex) % ALPHABET.length];
			// Find that middle rotor character on the outer rotor
			outerRotorCharIndex = outerRotorIndex[middleRotorCharacter];
			// Find the new inner rotor character corresponding to that outer rotor character.
			// Subtract from the inner rotor index the number of times the inner rotor has been rotated.
			numberOfRotorShifts = i % ALPHABET.length;
			incrementIndex = ALPHABET.length - numberOfRotorShifts;
			innerRotorCharacter = innerRotor[(outerRotorCharIndex + incrementIndex) % ALPHABET.length];

			cipherText[i] = innerRotorCharacter;
		}
		return cipherText;
	}

	@Override
	public String encrypt(final String plaintext, final String key) {
		validateKey(key);
		rotateRotors(key.charAt(0), key.charAt(1), key.charAt(2));
		return new String(encodeText(plaintext));
	}

	@Override
	public String decrypt(final String ciphertext, final String key) {
		validateKey(key);
		rotateRotors(key.charAt(0), key.charAt(1), key.charAt(2));
		return new String(decodeText(ciphertext.toCharArray())).toLowerCase();
	}

	private void validateKey(final String key) {
		if (key.length() != NUMBER_OF_ROTORS)
			throw new IllegalArgumentException("Key length must be equal to the number of rotors: " + NUMBER_OF_ROTORS);
	}
}