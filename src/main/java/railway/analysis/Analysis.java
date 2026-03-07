package railway.analysis;

public record Analysis(
		String ref,
		int minimumDelay,
		int lowerQuartileDelay,
		int medianDelay,
		int upperQuartileDelay,
		int maximumDelay,
		long  numStopsCancelled,
		double percentageDelay
		
) {}
