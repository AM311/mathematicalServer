package it.units.in0500908.mathematicalserver;

import it.units.in0500908.lineprocessingserver.LineProcessingServer;
import it.units.in0500908.mathematicalserver.processors.MathematicalRequestsProcessor;
import it.units.in0500908.mathematicalserver.processors.MathematicalResponsesBuilder;
import it.units.in0500908.utils.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * @author Alessio Manià - IN0500908
 */
public class Main {
	public static void main(String[] args) {
		try {
			int serverPort = Integer.parseInt(args[0]);
			LineProcessingServer server = new LineProcessingServer(serverPort, "BYE", new MathematicalRequestsProcessor(new MathematicalResponsesBuilder("OK", "ERR")), Executors.newCachedThreadPool());
			server.start();
		} catch (NullPointerException | NumberFormatException ex) {
			Logger.printLog(System.err, "Unable to create a new server: invalid port number. " + ex.getMessage());
		} catch (IOException | RuntimeException | Error ex) {
			Logger.printLog(System.err, "Unable to start MathematicalServer:  " + ex.getMessage());
		}
	}
}
