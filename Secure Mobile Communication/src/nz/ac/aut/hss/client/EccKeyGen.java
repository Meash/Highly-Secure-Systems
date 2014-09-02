package nz.ac.aut.hss.client;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyAgreement;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;
/**
 *
 * @author James Fairburn
 */
public class EccKeyGen {

    private static final BigInteger P 
	= new BigInteger("FFFFFFFF"+"00000001"+"00000000"+"00000000"+
			 "00000000"+"FFFFFFFF"+"FFFFFFFF"+"FFFFFFFF", 16);
    
    private static final BigInteger A 
	= new BigInteger("FFFFFFFF"+"00000001"+"00000000"+"00000000"+
			 "00000000"+"FFFFFFFF"+"FFFFFFFF"+"FFFFFFFC", 16);
    
    private static final BigInteger B 
	= new BigInteger("5AC635D8"+"AA3A93E7"+"B3EBBD55"+"769886BC"+
			 "651D06B0"+"CC53B0F6"+"3BCE3C3E"+"27D2604B", 16);
    
    
    private static final BigInteger GX
	= new BigInteger("6B17D1F2"+"E12C4247"+"F8BCE6E5"+"63A440F2"+
			 "77037D81"+"2DEB33A0"+"F4A13945"+"D898C296", 16);

    private static final BigInteger GY
	= new BigInteger("4FE342E2"+"FE1A7F9B"+"8EE7EB4A"+"7C0F9E16"+
			 "2BCE3357"+"6B315ECE"+"CBB64068"+"37BF51F5", 16);
    
    private static final BigInteger N
	= new BigInteger("FFFFFFFF"+"00000000"+"FFFFFFFF"+"FFFFFFFF"+
			 "BCE6FAAD"+"A7179E84"+"F3B9CAC2"+"FC632551", 16);
    private static final int H = 1;
    
    private ECPublicKey publicKey;
    private ECPrivateKey privateKey;
    private static ECParameterSpec specs;
    
    public EccKeyGen(){
        try {
            ECField feild = new ECFieldFp(P);
            EllipticCurve curve = new EllipticCurve(feild, A, B);
            ECPoint G = new ECPoint(GX, GY);
            specs = new ECParameterSpec(curve, G, N, H);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(specs);
            KeyPair keyPair = kpg.generateKeyPair();
            System.out.println("Finiched generating a key pair");
            publicKey = (ECPublicKey) keyPair.getPublic();
            privateKey = (ECPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EccKeyGen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(EccKeyGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public EccKeyGen(Context context){
        try {
            ECField feild = new ECFieldFp(P);
            EllipticCurve curve = new EllipticCurve(feild, A, B);
            ECPoint G = new ECPoint(GX, GY);
            specs = new ECParameterSpec(curve, G, N, H);
            privateKey = (ECPrivateKey) SaveLoadKeys.readPrivateKey(context);
            publicKey = (ECPublicKey) SaveLoadKeys.readPublicKey(context);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    public ECPublicKey getPublicKey() {
        return publicKey;
    }
    
    public ECPrivateKey getPrivateKey() {
		return privateKey;
	}
    
    public ECParameterSpec getSpecs(){
    	return specs;
    }
    
    public BigInteger secretKeyGen(ECPublicKey otherPublicKey){
        BigInteger key = null;
        try {
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            ka.init(privateKey);
            ka.doPhase(otherPublicKey, true); 
            key = new BigInteger(ka.generateSecret());
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EccKeyGen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EccKeyGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }
    
    
}
