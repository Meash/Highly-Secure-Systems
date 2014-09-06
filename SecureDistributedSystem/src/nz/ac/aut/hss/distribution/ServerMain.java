package nz.ac.aut.hss.distribution;

import nz.ac.aut.hss.distribution.server.KeyAuthorityServer;

/**
 * @author Martin Schrimpf
 * @created 03.09.2014
 */
public class ServerMain {
	public static void main(String[] args) throws Exception {
		final int port = 61001;
		final KeyAuthorityServer server = new KeyAuthorityServer(port);
		server.start();
		server.join();
		server.getKeyAuthority().storeState();
		System.out.println("Server stopped");
	}
}
