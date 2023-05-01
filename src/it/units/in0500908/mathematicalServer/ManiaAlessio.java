package it.units.in0500908.mathematicalServer;

import java.io.IOException;

/**
 * @author Alessio Manià - IN0500908
 */
public class ManiaAlessio {
	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);

			MyServer server = new MyServer(serverPort, "BYE");
			server.run();
		} catch (NullPointerException | NumberFormatException ex) {
			System.err.println("Invalid port number. " + ex);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
