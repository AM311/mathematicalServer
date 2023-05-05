package it.units.in0500908.mathematicalServer;

import it.units.in0500908.lineProcessingServer.LineProcessingServer;

/**
 * @author Alessio Manià - IN0500908
 */
public class MathematicalServer extends LineProcessingServer {

	public MathematicalServer(int port, String quitCommand) {
		super(port, quitCommand);
	}

	@Override
	public String process(String request) {
		return RequestsHandler.processRequest(request);
	}
}

