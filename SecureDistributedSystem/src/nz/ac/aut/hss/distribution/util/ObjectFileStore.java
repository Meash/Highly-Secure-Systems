package nz.ac.aut.hss.distribution.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * @author Martin Schrimpf
 * @created 26.08.2014
 */
public class ObjectFileStore {
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	private final Path path;
	private final ObjectSerializer serializer;

	public ObjectFileStore(final Path path) {
		this.path = path;
		serializer = new ObjectSerializer();
	}

	public void store(Object obj) throws IOException {
		final String serial = serializer.serialize(obj);
		Files.write(path, Arrays.asList(serial), ENCODING);
	}

	public Object retrieve() throws IOException, ClassNotFoundException {
		List<String> lines = Files.readAllLines(path, ENCODING);
		final int amountOfLines = lines.size();
		if (amountOfLines == 0)
			throw new IllegalStateException("File does not contain content");
		if (amountOfLines > 1)
			throw new IllegalStateException("File should only contain one line, contains " + amountOfLines);
		return serializer.deserialize(lines.get(0));
	}
}
