package it.units.in0500908.lineprocessingserver;

import it.units.in0500908.utils.Logger;

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

				writer.write(server.getRequestsProcessor().process(request));
				writer.flush();
			}
		} catch (IOException ex) {
			Logger.printLog(System.err, "Unhandled IO exception: " + ex.getMessage());
		} catch (NullPointerException ex) {
			Logger.printLog(System.err, "Client " + socket.getInetAddress() + " abruptly closed connection.");
		} catch (RuntimeException ex) {
			Logger.printLog(System.err, "Unhandled RuntimeException from:  " + socket.getInetAddress());
		} catch (Error er) {
			Logger.printLog(System.err, "ERROR: unable to guarantee proper server operation! " + er.getMessage());
		}

		Logger.printLog(System.out, "Closed connection: " + socket.getInetAddress());
	}
}
