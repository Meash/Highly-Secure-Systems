package nz.ac.aut.hss.distribution.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Martin Schrimpf
 * @created 06.09.2014
 */
public class HttpPostSender implements AutoCloseable {
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private final HttpURLConnection connection;
	private final OutputStreamWriter writer;
	private final BufferedReader reader;
	private static final String CHARSET = "UTF-8";

	public HttpPostSender(final String address) throws IOException {
		final URL url = new URL(address);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		writer = new OutputStreamWriter(connection.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}

	public void send(String label, String content) throws IOException {
		String body = label + "=" + URLEncoder.encode(content, CHARSET) + "&" +
				"param2=" + URLEncoder.encode("value2", "UTF-8");
		connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

		writer.write(body);
		writer.flush();
	}

	public String readReply() throws IOException {
		String reply = "";
		for (String line; (line = reader.readLine()) != null; ) {
			reply += line + LINE_SEPARATOR;
		}
		return reply;
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException ignored) {
		}
		try {
			reader.close();
		} catch (IOException ignored) {
		}
	}
}
