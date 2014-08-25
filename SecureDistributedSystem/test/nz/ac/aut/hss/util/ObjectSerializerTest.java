package nz.ac.aut.hss.util;

import nz.ac.aut.hss.distribution.util.ObjectSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ObjectSerializerTest {
	private ObjectSerializer serializer;

	@Before
	public void setUp() {
		serializer = new ObjectSerializer();
	}

	@Test
	public void string() throws Exception {
		final String str = "hey there";
		compare(str);
	}

	@Test
	public void map() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("a", "B");
		map.put("!", "c");
		compare(map);
	}

	private void compare(final Object obj) throws IOException, ClassNotFoundException {
		final String serial = serializer.serialize(obj);
		final Object actual = serializer.deserialize(serial);
		assertEquals(obj, actual);
	}
}
