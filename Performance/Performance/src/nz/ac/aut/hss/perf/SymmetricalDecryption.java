package nz.ac.aut.hss.perf;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

/**
 * @author Martin Schrimpf
 */
public interface SymmetricalDecryption {
    public String decrypt(String ciphertext) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException;
}
