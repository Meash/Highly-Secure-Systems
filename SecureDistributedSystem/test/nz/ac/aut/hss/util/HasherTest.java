package nz.ac.aut.hss.util;

import nz.ac.aut.hss.distribution.util.Hasher;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class HasherTest {
	private Hasher hasher;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		hasher = new Hasher();
	}

	@Test
	public void equal() throws UnsupportedEncodingException {
		final String source = "hello I am here to test the hashing!123";
		assertTrue(hasher.matchesHash(source, hasher.hash(source)));
	}
	@Test
	public void diff() throws UnsupportedEncodingException {
		final String str1 = "hello I am here to test the hashing!123";
		final String str2 = "hello I am different";
		assertFalse(hasher.matchesHash(str2, hasher.hash(str1)));
	}
}
