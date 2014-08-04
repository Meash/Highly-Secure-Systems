package nz.ac.aut.hss.perf;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zsb8604 on 30/07/2014.
 */
public class DefaultEncryptor {
    private final Key key;
    private final Cipher cipher;
    private final IvParameterSpec initVector;

    public DefaultEncryptor(String keyType, String cipherMode, String padding, boolean useIvParameter) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(keyType);
        this.key = keyGen.generateKey();
        this.cipher = Cipher.getInstance(keyType + "/" + cipherMode + "/" + padding);
        if (useIvParameter) {
            byte[] ivBytes = {51, 50, 7, -19, 120, 111, -110, 52, 9, -21, -6, -15, -95, 117, 36, -89};
            initVector = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
        } else {
            initVector = null;
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
    }

    public String encrypt(String plaintext) throws BadPaddingException, IllegalBlockSizeException {
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
        return new String(ciphertext);
    }

    public String decrypt(String ciphertext) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (initVector != null)
            cipher.init(Cipher.DECRYPT_MODE, key, initVector);
        else
            cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainBytes = cipher.doFinal(ciphertext.getBytes());
        return new String(plainBytes);
    }
}
