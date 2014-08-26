package nz.ac.aut.hss.encrypt;

import java.io.UnsupportedEncodingException;

public class RC4 {
    private static final String ENCODING = "ISO-8859-1" /* default encoding */;
    private byte[] S;
    private int a, b;

    private void init() {
        S = new byte[256];
        for (int i = 0; i < S.length; i++) {
            S[i] = (byte) i;
        }
    }

    private void permutateStates(String key) {
        char[] K = key.toCharArray();
        int m = K.length;
        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = mod(j + S[i] + K[i % m], 256);
            swap(S, i, j);
        }
        a = b = 0;
    }

    private void swap(byte[] a, int i, int j) {
        byte h = a[i];
        a[i] = a[j];
        a[j] = h;
    }

    private int mod(int x, int m) {
        x %= m;
        if (x < 0)
            x += m;
        return x;
    }

    private byte nextByte() {
        a = mod(a + 1, 256);
        b = mod(b + S[a], 256);
        swap(S, a, b);
        int t = mod(S[a] + S[b], 256);
        return S[t];
    }

    private String convert(String plaintext, String key) throws UnsupportedEncodingException {
        init();
        permutateStates(key);
        byte[] text = plaintext.getBytes(ENCODING);
        for (int i = 0; i < text.length; i++) {
            byte nextByte = nextByte();
            text[i] = (byte) (text[i] ^ nextByte);
        }
        return new String(text, ENCODING);
    }

    public String encrypt(String plaintext, String key) throws UnsupportedEncodingException {
        return convert(plaintext, key);
    }

    public String decrypt(String ciphertext, String key) throws UnsupportedEncodingException {
        return convert(ciphertext, key);
    }
}
