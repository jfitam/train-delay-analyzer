package railway.analysis;

public record Analysis(
		String ref,
		Integer minimumDelay,
		Integer lowerQuartileDelay,
		Integer medianDelay,
		Integer upperQuartileDelay,
		Integer maximumDelay,
		long  numStopsCancelled,
		Double percentageDelay
) { }
