package nz.ac.aut.hss.client.communication;

import android.content.Context;
import nz.ac.aut.hss.distribution.crypt.AsymmetricKeyUtil;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public class AndroidKeyStore implements KeyStore {
	private static final String KEY_FILE = "keypair.obj";
	private final AsymmetricKeyUtil keyUtil;
	private final ObjectSerializer serializer;
	private final Context context;
	private KeyPair keyPair;

	public AndroidKeyStore(final Context context) {
		this.context = context;
		serializer = new ObjectSerializer();
		keyUtil = new AsymmetricKeyUtil(RSA.ALGORITHM);
	}

	public KeyPair loadOrCreateAndSaveKeyPair()
			throws KeyStoreException {
		// memory
		if (keyPair != null)
			return keyPair;
		// file
		if (new File(KEY_FILE).exists()) {
			try {
				keyPair = loadKeyPair();
				return keyPair;
			} catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
				throw new KeyStoreException("Could not load key pair", e);
			}
		}
		// create
		else {
			try {
				keyPair = RSA.createKeyPair();
				saveKeyPair(keyPair);
				return keyPair;
			} catch (NoSuchAlgorithmException e) {
				throw new KeyStoreException("Could not create key pair", e);
			}
		}
	}

	private KeyPair loadKeyPair()
			throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
		BufferedReader br = null;
		try {
			InputStream inputStream = context.openFileInput("private_key.txt");
			InputStreamReader isr = new InputStreamReader(inputStream);
			br = new BufferedReader(isr);
			String publicKeyString = null, privateKeyString = null;
			String fullText = "";
			String line;
			while ((line = br.readLine()) != null) {
				fullText += line + "\n";
				if(publicKeyString == null)
					publicKeyString = line;
				else
					privateKeyString = line;
			}
//			final Object data = serializer.deserialize(fullText);
//			if (!(data instanceof SerializableKeyPair))
//				throw new IllegalStateException(
//						"Illegal content in file - expected SerializableKeyPair, got " + data.getClass().getName());
//			SerializableKeyPair serializedKeyPair = (SerializableKeyPair) data;
//			PrivateKey privateKey = keyUtil.toPrivateKey(serializedKeyPair.privateKey);
//			PublicKey publicKey = keyUtil.toPublicKey(serializedKeyPair.publicKey);
			PrivateKey privateKey = keyUtil.toPrivateKey(privateKeyString);
			PublicKey publicKey = keyUtil.toPublicKey(publicKeyString);
			return new KeyPair(publicKey, privateKey);
		} finally {
			if (br != null)
				br.close();
		}
	}

	private void saveKeyPair(KeyPair keyPair) throws KeyStoreException {
		OutputStreamWriter osw = null;
		try {
			final String privateKey = keyUtil.toString(keyPair.getPrivate());
			final String publicKey = keyUtil.toString(keyPair.getPublic());
//			final SerializableKeyPair serializableKeyPair = new SerializableKeyPair(privateKey, publicKey);
//			final String data = serializer.serialize(serializableKeyPair);
			FileOutputStream fos = context.openFileOutput(KEY_FILE, 0);
			osw = new OutputStreamWriter(fos);
//			osw.write(data);
			osw.write(publicKey + "\n");
			osw.write(privateKey);
		} catch (IOException e) {
			throw new KeyStoreException("Could not store key pair", e);
		} finally {
			if (osw != null)
				try {
					osw.close();
				} catch (IOException ignored) {
				}
		}
	}

	private class SerializableKeyPair implements Serializable {
		public final String privateKey, publicKey;

		private SerializableKeyPair(final String privateKey, final String publicKey) {
			this.privateKey = privateKey;
			this.publicKey = publicKey;
		}
	}
}
