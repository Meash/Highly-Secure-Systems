package nz.ac.aut.hss.distribution.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Martin Schrimpf
 * @created 25.08.2014
 */
public class KeyAuthorityServer extends Thread {
	private final ServerSocket serverSocket;
	private final KeyAuthority keyAuthority;

	public KeyAuthorityServer(final int port) throws IOException, ClassNotFoundException {
		serverSocket = new ServerSocket(port);
		keyAuthority = new KeyAuthority();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				try (Socket clientSocket = serverSocket.accept()) {
					final String id = clientSocket.getLocalAddress() + ":" + clientSocket.getPort();
					try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							try {
								final String response = keyAuthority.processInput(id, inputLine);
								if (response == null)
									break;
								if (response.isEmpty())
									continue;
								out.println(response);
							} catch (Throwable t) {
								t.printStackTrace();
								break;
							}
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} finally {
			try {
				keyAuthority.storeState();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
