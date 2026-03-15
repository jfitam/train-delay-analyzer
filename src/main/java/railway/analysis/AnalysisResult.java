package railway.analysis;

import java.util.List;

public record AnalysisResult(
	    String aggregationType,           
	    List<Analysis> stats         
	) { }
