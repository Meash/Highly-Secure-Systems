package nz.ac.aut.hss.perf;

import nz.ac.aut.hss.util.Stats;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by zsb8604 on 30/07/2014.
 */
public class PerformanceCompare {
    private static final int LOOPS = 1000;

    public static void main(String[] args) throws Exception /* derp */ {
        AES aes = new AES();
        DES des = new DES();
        int textLengthMin = 1;
        int textLengthMax = 100001;
        int textStepSize = 10000;
        new PerformanceCompare().compare(textLengthMin, textLengthMax, textStepSize, new SymmetricalEncryption[]{des, aes});
        new PerformanceCompare().compare(textLengthMin, textLengthMax, textStepSize, new SymmetricalDecryption[]{des, aes});
    }

    private void compare(int textLengthMin, int textLengthMax, int textStepSize, Operation[] operations) throws Exception {
        /* text lengths * encryptors */
        float[][] result = new float[(textLengthMax - textLengthMin) / textStepSize][operations.length];
        for (int textLength = textLengthMin; textLength < textLengthMax; textLength += textStepSize) {
            String[] plaintexts = generatePlaintexts(LOOPS, textLength);
            for (int e = 0; e < operations.length; e++) {
                Stats stats = new Stats();
                for (String plaintext : plaintexts) {
                    stats.startMeasurement();
                    operations[e].doOperation(plaintext);
                    stats.stopMeasurementAndAddValue();
                }
                result[(textLength - textLengthMin) / textStepSize][e] = stats.getExpectedValue();
            }
        }
        display("Encryption", result, textLengthMin, textStepSize);
    }

    private void display(String title, float[][] result, int firstValue, int stepSize) {
        System.out.println(title);
        int firstColWidth = 10;
        System.out.printf("%" + firstColWidth + "s\t%10s\t%10s\n", "Text length", "DES", "AES");
        for (int l = 0; l < result.length; l++) {
            int textLength = firstValue + l * stepSize;
            System.out.printf("%10s\t", textLength);
            for (int e = 0; e < result[l].length; e++) {
                System.out.printf("%10s\t", String.format("%1.3f", result[l][e]));
            }
            System.out.println();
        }
    }

    private void compare(int textLengthMin, int textLengthMax, int textStepSize, final SymmetricalEncryption[] encryptors) throws Exception {
        Operation[] operations = new Operation[encryptors.length];
        for (int i = 0; i < operations.length; i++) {
            final int finalI = i;
            operations[i] = new Operation() {
                public void doOperation(Object argument) throws Exception {
                    encryptors[finalI].encrypt((String) argument);
                }
            };
        }
        compare(textLengthMin, textLengthMax, textStepSize, operations);
    }

    private void compare(int textLengthMin, int textLengthMax, int textStepSize, final SymmetricalDecryption[] decryptors) throws Exception {
        Operation[] operations = new Operation[decryptors.length];
        for (int i = 0; i < operations.length; i++) {
            final int finalI = i;
            operations[i] = new Operation() {
                public void doOperation(Object argument) throws Exception {
                    decryptors[finalI].decrypt((String) argument);
                }
            };
        }
        compare(textLengthMin, textLengthMax, textStepSize, operations);
    }

    private String[] generatePlaintexts(int loops, int length) {
        final String[] result = new String[loops];
        for (int i = 0; i < result.length; i++) {
            result[i] = generatePlaintext(length);
        }
        return result;
    }

    private String generatePlaintext(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(length);
    }
}
