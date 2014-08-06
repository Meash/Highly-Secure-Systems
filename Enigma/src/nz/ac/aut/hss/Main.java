package nz.ac.aut.hss;

import nz.ac.aut.hss.cryptanalysis.EnigmaAnalyzer;
import nz.ac.aut.hss.encrypt.Enigma;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
	public static void main(String[] args) throws Exception /* since it's only a small fun-project... */ {
		try {
			new Main().handleArgs(args);
		} catch (IllegalArgumentException e) {
			error(e.getMessage());
		}
	}

	private void handleArgs(final String[] args) throws IllegalArgumentException, IOException {
		// first-level validation: duplicates
		validateArgs(args);

		// collect arguments
		Mode mode = null;
		String key = null, input = null;
		int rotors = 3;
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "-h":
				case "--help":
					usage();
					return;
				case "-e":
				case "--encrypt":
					mode = Mode.ENCRYPT;
					break;
				case "-d":
				case "--decrypt":
					mode = Mode.DECRYPT;
					break;
				case "-a":
				case "--attack":
					mode = Mode.ATTACK;
					break;
				case "-k":
				case "--key":
					if (i == args.length - 1) {
						throw new IllegalArgumentException("No key argument specified");
					}
					key = args[++i];
					break;
				case "-i":
				case "--input":
					if (i == args.length - 1) {
						throw new IllegalArgumentException("No input argument specified");
					}
					input = args[++i];
					break;
				case "-r":
				case "--rotors":
					if (i == args.length - 1) {
						throw new IllegalArgumentException("No rotor argument specified");
					}
					rotors = Integer.parseInt(args[++i]);
					break;
			}
		}

		// second-level validation: semantics
		if (mode == null) {
			throw new IllegalArgumentException("No mode specified");
		}

		switch (mode) {
			case ENCRYPT:
			case DECRYPT:
				if (key == null) {
					throw new IllegalArgumentException("No key specified");
				}
				if (input == null) {
					throw new IllegalArgumentException("No input specified");
				}
				break;
			case ATTACK:
				if (key != null) {
					System.out.println("Warning: Key set in attack mode will be ignored");
				}
				if (input == null) {
					throw new IllegalArgumentException("No input specified");
				}
				break;
		}

		// execute
		final Enigma enigma = new Enigma(rotors);
		switch (mode) {
			case ENCRYPT:
				System.out.printf("Encrypting the following plaintext with %d rotors and key %s: %s\n", rotors, key,
						input);
				final String ciphertext = enigma.encrypt(input, key);
				System.out.printf("%s: %s\n", "Ciphertext", ciphertext);
				break;
			case DECRYPT:
				System.out.printf("Decrypting the following ciphertext with %d rotors and key %s: %s\n", rotors, key,
						input);
				final String plaintext = enigma.decrypt(input, key);
				System.out.printf("%s: %s\n", "Plaintext", plaintext);
				break;
			case ATTACK:
				System.out.printf("Attacking the following ciphertext with %d rotors: %s\n", rotors, input);
				final String analyzedKey = new EnigmaAnalyzer(enigma).findKey(input);
				final String analyzedPlaintext = enigma.decrypt(input, analyzedKey);
				System.out.printf("%s: %s\n", "Plaintext", analyzedPlaintext);
				break;
		}
	}

	private void validateArgs(final String[] args) {
		Set<String> set = new HashSet<>();
		for (String arg : args) {
			if (set.contains(arg))
				throw new IllegalArgumentException("Argument " + arg + " specified twice");
			set.add(arg);
		}
	}

	private static void error(String message) {
		System.out.println("Error: " + message);
		usage();
	}

	private static void usage() {
		System.out.println("Usage: java -jar Enigma.jar");
		System.out.println("\t-e|--encrypt|-d|--decrypt|-a|--attack");
		System.out.println("\t-i|--input <input text> [-k|--key <key>] [-r|--rotors <amount of rotors>]");
	}

	private enum Mode {
		ENCRYPT, DECRYPT, ATTACK
	}
}
