package nz.ac.aut.hss.util;

import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ECCKeyGen {
	public static ECPublicKey create() {
		return new ECPublicKey() {
			@Override
			public ECPoint getW() {
				return null;
			}

			@Override
			public ECParameterSpec getParams() {
				return null;
			}

			@Override
			public String getAlgorithm() {
				return null;
			}

			@Override
			public String getFormat() {
				return null;
			}

			@Override
			public byte[] getEncoded() {
				return new byte[0];
			}
		}; // TODO
	}
}
