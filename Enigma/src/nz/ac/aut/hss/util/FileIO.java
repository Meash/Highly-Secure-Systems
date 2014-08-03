package nz.ac.aut.hss.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Martin Schrimpf
 * @created 03.08.2014
 */
public class FileIO {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Reads the content of a File.
	 * @param inputStream the input
	 * @return the String-content of the File
	 * @throws java.io.IOException           when the file could not be read
	 * @throws java.io.FileNotFoundException when the file does not exist
	 */
	public static String read(InputStream inputStream)
			throws IllegalArgumentException, IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null)
				result.append(line).append(LINE_SEPARATOR);
			return result.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
