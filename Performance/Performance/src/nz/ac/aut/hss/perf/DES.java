package nz.ac.aut.hss.perf;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zsb8604 on 30/07/2014.
 */
public class DES implements SymmetricalEncryption, SymmetricalDecryption {
    private final DefaultEncryptor encryptor;

    public DES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        encryptor = new DefaultEncryptor("DES", "CBC", "PKCS5Padding", false);
    }

    @Override
    public String encrypt(String plaintext) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        return encryptor.encrypt(plaintext);
    }

    @Override
    public String decrypt(String ciphertext) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return encryptor.decrypt(ciphertext);
    }
}
