package it.units.in0500908.lineProcessingServer;

import it.units.in0500908.mathematicalServer.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Alessio Manià - IN0500908
 */
public abstract class LineProcessingServer {
	private final int port;
	private final String quitCommand;

	public LineProcessingServer(int port, String quitCommand) {
		this.port = port;
		this.quitCommand = quitCommand;
	}

	public String getQuitCommand() {
		return quitCommand;
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

	public abstract String process(String request);

}
