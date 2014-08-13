package nz.ac.aut.hss.encrypt;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 13.08.2014
 */
public class RSAUtilTest {
	@Test
	public void stringToNum() {
		final String str = "test";
		BigInteger expected = new BigInteger(str.getBytes());
		BigInteger actual = RSAUtil.toNumber(str);
		assertEquals(expected, actual);
	}

	@Test
	public void numToString() {
		final BigInteger num = BigInteger.valueOf(1952805748);
		final String expected = new String(num.toByteArray());
		final String actual = RSAUtil.toString(num);
		assertEquals(expected, actual);
	}

	@Test
	public void numberStringConversion() {
		final String original = "waddup this is working pretty well";
		BigInteger num = RSAUtil.toNumber(original);
		final String actual = RSAUtil.toString(num);
		assertEquals(original, actual);
	}
}
