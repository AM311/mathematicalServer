package it.units.in0500908.mathematicalserver.processors.specificrequestsprocessors;

import it.units.in0500908.lineprocessingserver.SpecificRequestsProcessor;
import it.units.in0500908.lineprocessingserver.StatisticsCounter;
import it.units.in0500908.mathematicalserver.InvalidRequestException;
import it.units.in0500908.utils.NumbersFormatter;

/**
 * @author Alessio Manià - IN0500908
 */
public class StatRequestsProcessor implements SpecificRequestsProcessor {
	private final StatisticsCounter statisticsCounter;                    	//Necessario per accedere alle statistiche
	private final String request;                                       	//Necessario: callable non accetta argomenti

	public StatRequestsProcessor(StatisticsCounter statisticsCounter, String request) {
		this.statisticsCounter = statisticsCounter;
		this.request = request;
	}

	public static boolean isStatRequest(String request) {
		return StatRequestToken.isValid(request);
	}

	@Override
	public String call() throws InvalidRequestException {                //Passaggio a ENUM forza a gestire tutti i casi
		return switch (StatRequestToken.parse(request)) {
			case REQS -> String.valueOf(statisticsCounter.getNumOfResponses());
			case AVG -> NumbersFormatter.millisFormat(statisticsCounter.getAvgResponseTime());
			case MAX -> NumbersFormatter.millisFormat(statisticsCounter.getMaxResponseTime());
		};
	}

	//========================

	public enum StatRequestToken {
		REQS("STAT_REQS"), AVG("STAT_AVG_TIME"), MAX("STAT_MAX_TIME");

		private final String tokenString;

		StatRequestToken(String tokenString) {
			this.tokenString = tokenString;
		}

		public static boolean isValid(String request) {							//NO INTERFACCIA: DEVONO ESSERE METODI STATICI!
			for (StatRequestToken token : StatRequestToken.values()) {
				if (token.tokenString.equals(request)) {
					return true;
				}
			}

			return false;
		}

		public static StatRequestToken parse(String request) throws InvalidRequestException {
			for (StatRequestToken token : StatRequestToken.values()) {
				if (token.tokenString.equals(request)) {
					return token;
				}
			}

			throw new InvalidRequestException("Invalid Stat Request!");
		}

		public String getTokenString() {
			return this.tokenString;
		}
	}
}
