package railway.analysis;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import railway.model.DelayRecord;
import railway.table.AggregationLevel;
import railway.table.DataTable;

@Service
public class DelayAnalysisService {
	
	DataTable storage; 
	
	@Autowired
	public DelayAnalysisService(DataTable storage) {
		this.storage = storage;
	}
	
	public AnalysisResult getAllTrains() {
		return computeStatistics(storage.getAll(), AggregationLevel.TRAIN_SERVICE);
	}


	public AnalysisResult getTrain(String trainNumber) {
		return computeStatistics(storage.get(trainNumber, null, null, null), AggregationLevel.TRAIN_SERVICE);
	}

	public AnalysisResult getAllStations() {
		return computeStatistics(storage.getAll(), AggregationLevel.STATION);
	}

	public AnalysisResult getStation(String stationName) {
		return computeStatistics(storage.get(null, stationName, null, null), AggregationLevel.STATION);
	}

	public AnalysisResult getAllServices() {
		return computeStatistics(storage.getAll(), AggregationLevel.SERVICE);
	}

	public AnalysisResult getServices(String serviceName) {
		return computeStatistics(storage.get(null, null, serviceName, null), AggregationLevel.SERVICE);
	}

	public AnalysisResult getAllCompanies() {
		return computeStatistics(storage.getAll(), AggregationLevel.COMPANY);
	}

	public AnalysisResult getCompany(String companyName) {
		return computeStatistics(storage.get(null, null, null, companyName), AggregationLevel.COMPANY);
	}


	/*******************************************************************************************/
	
	private AnalysisResult computeStatistics(List<DelayRecord> records, AggregationLevel by) {
		Map<String, List<DelayRecord>> grouped = records.stream()
			    .collect(Collectors.groupingBy(r -> by.keyExtractor.apply(r)));

			List<Analysis> stats = grouped.entrySet().stream()
			    .map(entry -> buildAnalysis(entry.getKey(), entry.getValue()))
			    .toList();	
			
			return new AnalysisResult(by.kind, stats);
	}
	
	private Analysis buildAnalysis(String key, List<DelayRecord> group ) {
	    List<Integer> delays = group
	    		.stream()
	    		.filter(r -> !r.isStopCancelled())
	    		.map(DelayRecord::getDelay)
	    		.sorted()
	    		.toList();
	    int n = delays.size();
	    return new Analysis(
	        key,
	        delays.get(0),
	        delays.get(Math.max(0, n/4-1)),
	        delays.get(Math.max(0,n/2-1)),
	        delays.get(Math.max(0,3*n/4-1)),
	        delays.get(Math.max(0,n-1)),
	        (int) group.stream().filter(DelayRecord::isStopCancelled).count(),
	        group.stream().filter(DelayRecord::isDelayed).count() * 100.0 / n
	    );
	}


}