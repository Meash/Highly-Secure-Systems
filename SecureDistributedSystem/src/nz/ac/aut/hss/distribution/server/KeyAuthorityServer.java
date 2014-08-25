package nz.ac.aut.hss.distribution.server;

import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.protocol.ProtocolInvalidationMessage;
import nz.ac.aut.hss.distribution.protocol.SuppressedMessage;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

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
	private final ObjectSerializer serializer;

	public KeyAuthorityServer(final int port) throws IOException, ClassNotFoundException {
		serverSocket = new ServerSocket(port);
		keyAuthority = new KeyAuthority();
		serializer = new ObjectSerializer();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try (Socket clientSocket = serverSocket.accept()) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						final Object inputObject = serializer.deserialize(inputLine);
						if (!(inputObject instanceof Message))
							throw new IllegalArgumentException(
									"Expected message object, got " + inputObject.getClass().getName());
						final Message input = (Message) inputObject;
						if (input instanceof ProtocolInvalidationMessage) {
							System.err.println(
									"Received protocol invalidation: " + ((ProtocolInvalidationMessage) input).message);
							break;
						}
						final Message output = keyAuthority.processInput(input);
						if (output != null) {
							if (!(output instanceof SuppressedMessage)) { // ignore
								final String outputString = serializer.serialize(output);
								out.println(outputString);
							}
						} else
							break;
					}
				} catch (ProcessingException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new KeyAuthorityServer(61001).start();
	}
}
