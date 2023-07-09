package it.units.in0500908.lineprocessingserver;

import it.units.in0500908.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author Alessio Manià - IN0500908
 */
public class LineProcessingServer {
	private final int port;
	private final String quitCommand;
	private final RequestsProcessor requestsProcessor;
	private final ExecutorService executorService;

	public LineProcessingServer(int port, String quitCommand, RequestsProcessor requestsProcessor, ExecutorService executorService) {
		this.port = port;
		this.quitCommand = quitCommand;
		this.requestsProcessor = requestsProcessor;
		this.executorService = executorService;
	}

	public int getPort() {
		return port;
	}

	public String getQuitCommand() {
		return quitCommand;
	}

	public RequestsProcessor getRequestsProcessor() {
		return requestsProcessor;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	//========================
	public void start() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port); requestsProcessor) {
			while (true) {
				try {
					final Socket socket = serverSocket.accept();

					executorService.submit(() -> {
						try (socket) {
							BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

							Logger.printLog(System.out, "New connection: " + socket.getInetAddress());

							while (true) {
								String request = reader.readLine();

								if (request.equals(quitCommand)) {
									break;
								}

								writer.write(requestsProcessor.process(request) + System.lineSeparator());
								writer.flush();
							}
						} catch (NullPointerException ex) {
							Logger.printLog(System.err, "Client " + socket.getInetAddress() + " abruptly closed connection.");
						} catch (IOException ex) {
							Logger.printLog(System.err, "Unhandled IO exception: " + ex.getMessage());
						} catch (RuntimeException ex) {
							Logger.printLog(System.err, "Unhandled RuntimeException while managing " + socket.getInetAddress() + ": " + ex.getMessage());
						} catch (Error er) {
							Logger.printLog(System.err, "ERROR while managing " + socket.getInetAddress() + ": " + er.getMessage());
						}

						Logger.printLog(System.out, "Closed connection: " + socket.getInetAddress());
					});
				} catch (IOException ex) {
					Logger.printLog(System.err, "Unable to accept connection: " + ex.getMessage());
				} catch (RuntimeException ex) {
					Logger.printLog(System.err, "RuntimeException trying managing new client connection: " + ex.getMessage());
				}
			}
		} catch (IOException | RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			Logger.printLog(System.err, "Exception trying closing resources: " + ex);
		} finally {
			executorService.shutdown();
		}
	}
}
