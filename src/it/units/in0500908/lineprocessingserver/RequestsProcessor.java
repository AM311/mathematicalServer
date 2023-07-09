package it.units.in0500908.lineprocessingserver;

/**
 * @author Alessio Manià - IN0500908
 */
public interface RequestsProcessor extends AutoCloseable{
	String process(String request);
}
