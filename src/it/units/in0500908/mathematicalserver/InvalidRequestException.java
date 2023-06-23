package it.units.in0500908.mathematicalserver;

/**
 * @author Alessio Manià - IN0500908
 */
public class InvalidRequestException extends Exception{
	public InvalidRequestException() {
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestException(Throwable cause) {
		super(cause);
	}

	//--------------------------


	@Override
	public String getLocalizedMessage() {
		try {
			return getMessage() + ": " + getCause().getMessage();
		} catch (NullPointerException ex) {
			return getMessage();
		}
	}
}
