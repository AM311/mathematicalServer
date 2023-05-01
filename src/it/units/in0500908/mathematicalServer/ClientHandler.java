package it.units.in0500908.mathematicalServer;

import java.io.*;
import java.net.Socket;

/**
 * @author Alessio Manià - IN0500908
 */
public class ClientHandler extends Thread {
	private final Socket socket;
	private final String quitCommand;

	public ClientHandler(Socket socket, String quitCommand) {
		this.socket = socket;
		this.quitCommand = quitCommand;
	}

	@Override
	public void run() {
		try(socket) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			//todo sistemare i log
			System.out.println("Connesso: " + socket.getInetAddress());

			while(true) {
				String request = reader.readLine();

				if(request.equals(quitCommand)) {
					break;
				}

				String response = RequestsHandler.processRequest(request);
				writer.write(response);
				writer.flush();
			}


		} catch (IOException ex) {
			System.err.println("Exception!");		//todo
		}

		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//--------------


}
