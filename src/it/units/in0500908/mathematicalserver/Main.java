package it.units.in0500908.mathematicalserver;

import it.units.in0500908.lineprocessingserver.LineProcessingServer;
import it.units.in0500908.mathematicalserver.handlers.MathematicalRequestsProcessor;
import it.units.in0500908.utils.Logger;

import java.io.IOException;

/**
 * @author Alessio Manià - IN0500908
 */
public class Main {
	private static final String quitCommand = "BYE";

	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);
			LineProcessingServer server = new LineProcessingServer(serverPort, quitCommand, new MathematicalRequestsProcessor());
			server.run();
		} catch (NullPointerException | NumberFormatException ex) {
			Logger.printLog(System.err, "Unable to create a new server: invalid port number. " + ex);
		} catch (IOException e) {
			Logger.printLog(System.err, "Unable to start MathematicalServer: " + e);
		}
	}
}
