package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public abstract class ResponsesProcessor {
	protected final String okResponsesMessage;
	protected final String errResponsesMessage;

	public ResponsesProcessor(String okResponsesMessage, String errResponsesMessage) {
		this.okResponsesMessage = okResponsesMessage;
		this.errResponsesMessage = errResponsesMessage;
	}

	//=======================

	public abstract String buildOkResponse(String message);
	public abstract String buildErrResponse(String message);
}
