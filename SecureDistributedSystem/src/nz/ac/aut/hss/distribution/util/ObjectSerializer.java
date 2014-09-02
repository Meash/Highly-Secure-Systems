package nz.ac.aut.hss.distribution.util;

import java.io.*;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ObjectSerializer {
	public String serialize(Object obj) throws IOException {
		if (obj == null)
			throw new IllegalArgumentException("object must not be null");
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
			return new String(Base64Coder.encode(baos.toByteArray()));
		}
	}

	public Object deserialize(String serializedObject) throws IOException, ClassNotFoundException {
		if (serializedObject == null)
			throw new IllegalArgumentException("serial must not be null");
		byte[] data = Base64Coder.decode(serializedObject);
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
			return ois.readObject();
		}
	}
}
