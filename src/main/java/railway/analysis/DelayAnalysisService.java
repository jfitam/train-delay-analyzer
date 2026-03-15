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
final public class DelayAnalysisService {
	private DataTable storage; 
	
	/**
	 * Constructor
	 * @param storage
	 */
	@Autowired
	public DelayAnalysisService(DataTable storage) {
		this.storage = storage;
	}
	
	/**
	 * Perform Analysis for all trains
	 * @return AnalysisResult 
	 */
	public AnalysisResult getAllTrains() {
		return computeStatistics(
				storage.getAll(), 
				AggregationLevel.TRAIN_SERVICE
				);
	}

	/**
	 * Perform Analysis for specific train
	 * @return AnalysisResult 
	 */
	public AnalysisResult getTrain(String trainNumber) {
		return computeStatistics(
				storage.get(
						trainNumber, 
						null, 
						null, 
						null), 
				AggregationLevel.TRAIN_SERVICE
				);
	}

	/**
	 * Perform Analysis for all stations
	 * @return AnalysisResult 
	 */
	public AnalysisResult getAllStations() {
		return computeStatistics(
				storage.getAll(), 
				AggregationLevel.STATION
				);
	}

	/**
	 * Perform Analysis for specific station
	 * @return AnalysisResult 
	 */
	public AnalysisResult getStation(String stationName) {
		return computeStatistics(
				storage.get(
						null, 
						stationName, 
						null, 
						null), 
				AggregationLevel.STATION
				);
	}

	/**
	 * Perform Analysis for all services
	 * @return AnalysisResult 
	 */
	public AnalysisResult getAllServices() {
		return computeStatistics(
				storage.getAll(), 
				AggregationLevel.SERVICE
				);
	}

	/**
	 * Perform Analysis for specific service
	 * @return AnalysisResult 
	 */
	public AnalysisResult getServices(String serviceName) {
		return computeStatistics(
				storage.get(
						null, 
						null, 
						serviceName, 
						null), 
				AggregationLevel.SERVICE
				);
	}
	
	/**
	 * Perform Analysis for all companies
	 * @return AnalysisResult 
	 */
	public AnalysisResult getAllCompanies() {
		return computeStatistics(
				storage.getAll(), 
				AggregationLevel.COMPANY
				);
	}

	/**
	 * Perform Analysis for specific company
	 * @return AnalysisResult 
	 */
	public AnalysisResult getCompany(String companyName) {
		return computeStatistics(
				storage.get(
						null, 
						null, 
						null, 
						companyName), 
				AggregationLevel.COMPANY
				);
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
	    
	    if (delays.size() == 0) { 
		    return new Analysis(
			        key,
			        null,
			        null,
			        null,
			        null,
			        null,
			        (int) group.stream().filter(DelayRecord::isStopCancelled).count(),
			        null
			    );
	    	}
	    
	    int n = delays.size();
	    return new Analysis(
	        key,
	        delays.get(0),
	        delays.get(Math.max(0, n / 4 - 1 )),
	        delays.get(Math.max(0, n / 2 - 1 )),
	        delays.get(Math.max(0, 3 * n / 4 - 1 )),
	        delays.get(Math.max(0, n - 1 )),
	        (int) group.stream().filter(DelayRecord::isStopCancelled).count(),
	        group.stream().filter(DelayRecord::isDelayed).count() * 100.0 / n
	    );
	}


}