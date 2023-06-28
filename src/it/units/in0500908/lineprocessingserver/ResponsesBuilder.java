package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public interface ResponsesBuilder {
	String buildOkResponse(String message);
	String buildErrResponse(String message);
}
