package it.units.in0500908.lineprocessingserver;

import it.units.in0500908.mathematicalserver.InvalidRequestException;

/**
 * @author Alessio Manià - IN0500908
 */
public interface SpecificRequestProcessor {
	String process(String request) throws InvalidRequestException;
}
