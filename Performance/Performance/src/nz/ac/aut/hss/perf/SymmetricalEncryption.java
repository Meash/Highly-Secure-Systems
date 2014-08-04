package nz.ac.aut.hss.perf;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

/**
 * @author Martin Schrimpf
 */
public interface SymmetricalEncryption {
    public String encrypt(String plaintext) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException;
}
