package it.units.in0500908.lineprocessingserver;

import it.units.in0500908.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Alessio Manià - IN0500908
 */
public class LineProcessingServer {
	private final int port;
	private final String quitCommand;
	private final RequestsProcessor requestsProcessor;

	public LineProcessingServer(int port, String quitCommand, RequestsProcessor requestsProcessor) {
		this.port = port;
		this.quitCommand = quitCommand;
		this.requestsProcessor = requestsProcessor;
	}

	public String getQuitCommand() {
		return quitCommand;
	}

	public RequestsProcessor getRequestsProcessor() {
		return requestsProcessor;
	}

	//========================
	public void run() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket socket;
				try {
					socket = serverSocket.accept();
					ClientHandler clientHandler = new ClientHandler(socket, this);
					clientHandler.start();
				} catch (IOException ex) {
					Logger.printLog(System.err, "Unable to accept connection: " + ex);
				}
			}
		}
	}
}
