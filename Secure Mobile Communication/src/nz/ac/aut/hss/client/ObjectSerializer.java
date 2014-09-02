package nz.ac.aut.hss.client;

import java.io.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;


/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class ObjectSerializer {
	public static String serialize(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		return new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
	}

	
	public static Object deserialize(String str) throws IOException, ClassNotFoundException {
		byte[] data = Base64.decode(str, Base64.DEFAULT);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		return ois.readObject();
	}
}
