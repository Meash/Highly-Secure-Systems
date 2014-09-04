package nz.ac.aut.hss.client.communication;

import android.content.Context;
import nz.ac.aut.hss.distribution.crypt.RSA;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 04.09.2014
 */
public class AndroidKeyStore implements KeyStore {
	private static final String KEY_FILE = "keypair.obj";
	private final ObjectSerializer serializer;
	private final Context context;

	public AndroidKeyStore(final Context context) {
		this.context = context;
		serializer = new ObjectSerializer();
	}

	public KeyPair loadOrCreateAndSaveKeyPair()
			throws KeyStoreException {
		if (new File(KEY_FILE).exists()) {
			try {
				try {
					return loadKeyPair();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch ( ClassNotFoundException e) {
				throw new KeyStoreException("Could not load key pair", e);
			}
		} else {
			try {
				final KeyPair keyPair = RSA.createKeyPair();
				saveKeyPair(keyPair);
				return keyPair;
			} catch (NoSuchAlgorithmException e) {
				throw new KeyStoreException("Could not create key pair", e);
			}
		}
		return null;
	}

	private KeyPair loadKeyPair() throws IOException, ClassNotFoundException {
		BufferedReader br = null;
		try {
			InputStream inputStream = context.openFileInput("private_key.txt");
			InputStreamReader isr = new InputStreamReader(inputStream);
			br = new BufferedReader(isr);
			String fullText = "";
			String line;
			while ((line = br.readLine()) != null) {
				fullText += line;
			}
			final Object data = serializer.deserialize(fullText);
			if (!(data instanceof KeyPair))
				throw new IllegalStateException(
						"Illegal content in file - expected KeyPair, got " + data.getClass().getName());
			return (KeyPair) data;
		} finally {
			if (br != null)
				br.close();
		}
	}

	private void saveKeyPair(KeyPair keyPair) throws KeyStoreException {
		OutputStreamWriter osw = null;
		try {
			final String data = serializer.serialize(keyPair);
			FileOutputStream fos = context.openFileOutput(KEY_FILE, 0);
			osw = new OutputStreamWriter(fos);
			osw.write(data);
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
}
