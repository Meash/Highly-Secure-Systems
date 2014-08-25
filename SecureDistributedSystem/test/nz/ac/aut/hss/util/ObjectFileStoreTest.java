package nz.ac.aut.hss.util;

import nz.ac.aut.hss.distribution.util.ObjectFileStore;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ObjectFileStoreTest {
	private static final Path PATH = Paths.get("test-file.obj");
	private static final ObjectFileStore store = new ObjectFileStore(PATH);

	@Test
	public void string() throws IOException, ClassNotFoundException {
		final String str = "Hey there!";
		store.store(str);
		final Object actual = store.retrieve();
		assertEquals(str, actual);
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		Files.delete(PATH);
	}
}
