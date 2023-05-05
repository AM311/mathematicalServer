package it.units.in0500908.lineProcessingServer;

import it.units.in0500908.mathematicalServer.Logger;

import java.io.*;
import java.net.Socket;

/**
 * @author Alessio Manià - IN0500908
 */
public class ClientHandler extends Thread {
	private final Socket socket;
	private final LineProcessingServer server;

	public ClientHandler(Socket socket, LineProcessingServer server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try(socket) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			Logger.printLog(System.out, "New connection: " + socket.getInetAddress());

			while(true) {
				String request = reader.readLine();

				if(request.equals(server.getQuitCommand())) {
					break;
				}

				writer.write(server.process(request));
				writer.flush();
			}
		} catch (IOException | NullPointerException ex) {
			Logger.printLog(System.err, "Unhandled exception: " + ex.getMessage());
		}

		try {
			socket.close();
			Logger.printLog(System.out, "Closed connection: " + socket.getInetAddress());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
