package it.units.in0500908.lineProcessingServer;

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
		ServerSocket serverSocket = new ServerSocket(port);

		while (true) {
			Socket socket = serverSocket.accept();
			ClientHandler clientHandler = new ClientHandler(socket, this);
			clientHandler.start();
		}
	}

	public abstract String process(String request);

}
