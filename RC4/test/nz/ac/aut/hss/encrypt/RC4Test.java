package nz.ac.aut.hss.encrypt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RC4Test {
    private RC4 rc4;

    @Before
    public void setUp() {
        rc4 = new RC4();
    }

    @Test
    public void encryptDecrypt() throws Exception {
        final String plain = "hello there";
        System.out.println("Plain:  " + plain);
        final String key = "iamakey";
        final String cipher = rc4.encrypt(plain, key);
        System.out.println("Cipher: " + cipher);
        assertEquals(plain, rc4.decrypt(cipher, key));
    }
}
