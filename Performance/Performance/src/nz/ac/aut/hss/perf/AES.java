package nz.ac.aut.hss.perf;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zsb8604 on 30/07/2014.
 */
public class AES implements SymmetricalEncryption, SymmetricalDecryption {
    private final DefaultEncryptor encryptor;

    public AES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        encryptor = new DefaultEncryptor("AES", "CBC", "PKCS5Padding", true);
    }

    @Override
    public String encrypt(String plaintext) throws BadPaddingException, IllegalBlockSizeException {
        return encryptor.encrypt(plaintext);
    }

    @Override
    public String decrypt(String ciphertext) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return encryptor.decrypt(ciphertext);
    }
}
