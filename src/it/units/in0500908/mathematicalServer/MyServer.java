package it.units.in0500908.mathematicalServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Alessio Manià - IN0500908
 */
public class MyServer {
	private final int portNumber;
	private final String quitCommand;

	public MyServer(int portNumber, String quitCommand) {
		this.portNumber = portNumber;
		this.quitCommand = quitCommand;
	}

	public void run() throws IOException {
		ServerSocket serverSocket = new ServerSocket(portNumber);

		while (true) {
			Socket socket = serverSocket.accept();

			ClientHandler clientHandler = new ClientHandler(socket, quitCommand);
			clientHandler.start();
		}
	}


}

