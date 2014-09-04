package nz.ac.aut.hss.distribution.crypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martin Schrimpf
 * @created 28.08.2014
 */
public class ECCEncryption implements Encryption {
	private static final BigInteger P
			= new BigInteger("FFFFFFFF" + "00000001" + "00000000" + "00000000" +
			"00000000" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF", 16);

	private static final BigInteger A
			= new BigInteger("FFFFFFFF" + "00000001" + "00000000" + "00000000" +
			"00000000" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFC", 16);

	private static final BigInteger B
			= new BigInteger("5AC635D8" + "AA3A93E7" + "B3EBBD55" + "769886BC" +
			"651D06B0" + "CC53B0F6" + "3BCE3C3E" + "27D2604B", 16);


	private static final BigInteger GX
			= new BigInteger("6B17D1F2" + "E12C4247" + "F8BCE6E5" + "63A440F2" +
			"77037D81" + "2DEB33A0" + "F4A13945" + "D898C296", 16);

	private static final BigInteger GY
			= new BigInteger("4FE342E2" + "FE1A7F9B" + "8EE7EB4A" + "7C0F9E16" +
			"2BCE3357" + "6B315ECE" + "CBB64068" + "37BF51F5", 16);

	private static final BigInteger N
			= new BigInteger("FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF" +
			"BCE6FAAD" + "A7179E84" + "F3B9CAC2" + "FC632551", 16);
	private static final int H = 1;

	private static final String TRANSFORMATION = "ECIES";
	private static final String PROVIDER = "BC";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private final Key key2;
	private final Key key1;
	private static ECParameterSpec specs;

	public ECCEncryption() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException,
			NoSuchPaddingException {
		init();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(TRANSFORMATION);
		kpg.initialize(specs);
		KeyPair keyPair = kpg.generateKeyPair();
		System.out.println("Finished generating a key pair");
		this.key2 = keyPair.getPublic();
		this.key1 = keyPair.getPrivate();
	}

	public ECCEncryption(Key key1, Key key2)
			throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
		init();
		this.key1 = key1;
		this.key2 = key2;
	}

	private static void init() throws NoSuchProviderException {
		if (Security.getProvider("BC") == null) {
			throw new NoSuchProviderException("Provider BouncyCastle is not available");
		}
		if (specs == null) {
			ECField field = new ECFieldFp(P);
			EllipticCurve curve = new EllipticCurve(field, A, B);
			ECPoint G = new ECPoint(GX, GY);
			specs = new ECParameterSpec(curve, G, N, H);
		}
	}


	@Override
	public String encrypt(final String plain) throws CryptException {
		return convert(plain, Cipher.ENCRYPT_MODE);
	}

	@Override
	public String decrypt(final String cipher) throws CryptException {
		return convert(cipher, Cipher.DECRYPT_MODE);
	}

	private String convert(final String input, final int mode) throws CryptException {
		try {
			final Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			final Key key = mode == Cipher.ENCRYPT_MODE ? key1 : key2;
			if (key == null)
				throw new IllegalStateException("Key is null");
			cipher.init(mode, key);
			final byte[] bytes = input.getBytes(Encryption.CHARSET);
			final byte[] result = cipher.doFinal(bytes);
			return new String(result, Encryption.CHARSET);
		} catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException
				| NoSuchProviderException
				| InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptException(e);
		}
	}

	public BigInteger secretKeyGen(ECPublicKey otherPublicKey) {
		BigInteger key = null;
		try {
			KeyAgreement ka = KeyAgreement.getInstance(TRANSFORMATION);
			ka.init(key1);
			ka.doPhase(otherPublicKey, true);
			key = new BigInteger(ka.generateSecret());
		} catch (InvalidKeyException | NoSuchAlgorithmException ex) {
			Logger.getLogger(ECCEncryption.class.getName()).log(Level.SEVERE, null, ex);
		}
		return key;
	}

	public Key getKey2() {
		return key2;
	}

	public Key getKey1() {
		return key1;
	}

	public static KeyPair createKeyPair() throws CryptException {
		try {
			init();
			final KeyPairGenerator kpg = KeyPairGenerator.getInstance(TRANSFORMATION, PROVIDER);
			kpg.initialize(specs);
			return kpg.genKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
			throw new CryptException(e);
		}
	}
}
