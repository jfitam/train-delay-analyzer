package railway.table;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import railway.io.CsvParser;
import railway.io.DatasetDownloader;
import railway.model.DelayRecord;

@Component
public class DataTable implements Consumer<DelayRecord>{
	
	private List<DelayRecord> records;
	private Map<String, List<Integer>> indexTrainNumber;
	private Map<String, List<Integer>> indexStation;
	private Map<String, List<Integer>> indexService;
	private Map<String, List<Integer>> indexCompany;
	
	public DataTable() {
		records = new ArrayList<>();
		indexTrainNumber = new HashMap<>();
		indexStation = new HashMap<>();
		indexService = new HashMap<>();
		indexCompany = new HashMap<>();
		new HashMap<>();
	}
	
	@PostConstruct
	private void load_data() throws MalformedURLException, IOException, URISyntaxException {
		//fetch
		String url = "https://opendata.rijdendetreinen.nl/public/services/services-2025.csv.gz";			
		Queue<String> raw_data = (Queue<String>) DatasetDownloader.fetch(url);
		
		//convert
		CsvParser.parse(raw_data, this);
	}

	/**
	 * Save one element and update the indexes.
	 * @param newRecord the new records to save.
	 */
	@Override
	public void accept(DelayRecord newRecord) {
		if(newRecord == null) { return; }
		if(!newRecord.isScheduled()) {return; }
		
		int i = records.size();
		records.add(newRecord);
		
		addIndex(indexTrainNumber, newRecord.getTrainNumber(), i);
		addIndex(indexStation, newRecord.getStationName(), i);
		addIndex(indexService, newRecord.getService(), i);
		addIndex(indexCompany, newRecord.getCompany(), i);

	}

	
	/**
	 * Retrieve the records that match the conditions given. The parameters can be null and they will be ignored.
	 * 
	 * @param trainNumber 
	 * @param stationName
	 * @param serviceName
	 * @param companyName
	 * @return the recordset after filtering.
	 */
	public List<DelayRecord> get(
			String trainNumber, 
			String stationName, 
			String serviceName, 
			String companyName){
		List<List<Integer>> indexes = new ArrayList<List<Integer>>();
		
		indexes.add(getIndex(indexTrainNumber, trainNumber)); 
		indexes.add(getIndex(indexStation, stationName)); 
		indexes.add(getIndex(indexService, serviceName)); 
		indexes.add(getIndex(indexCompany, companyName)); 
		
		return filter(indexes);
	}
	
	/**
	 * Retrieve all records without filtering.
	 * @return all records
	 */
	public List<DelayRecord> getAll(){ return records; }

	
	/********************************************************************************************/
	
	/*
	 * Add an entry to a specific index
	 */
	private void addIndex(Map<String, List<Integer>> index, String key, int i) {
		index.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
	}
	

	/*
	 * apply filtering to the total list of records
	 */
	private List<DelayRecord> filter(List<List<Integer>> indexes) {
		indexes.removeIf(Objects::isNull);
		indexes.removeIf(List::isEmpty);
		
		if (indexes.isEmpty()) { return records; }
		
		List<DelayRecord> resultRecords = new ArrayList<>();
		
		int[] pointers = new int[indexes.size()];	
		
		while (IntStream.range(0, pointers.length)
                .allMatch(p -> pointers[p] < indexes.get(p).size())) {

			int min = IntStream.range(0, pointers.length)
	                   .map(p -> indexes.get(p).get(pointers[p]))
	                   .min()
	                   .orElseThrow();
			
			int max = IntStream.range(0, pointers.length)
	                   .map(p -> indexes.get(p).get(pointers[p]))
	                   .max()
	                   .orElseThrow();

		    if(min == max) {
		    	// value found. advance all the pointers
		    	resultRecords.add(records.get(min));
		    	for(int p = 0; p < pointers.length; p++) {
		    		pointers[p] += 1;
		    	}   
		    } else {
		    	//advance the lowest pointers
		    	for(int p = 0; p < pointers.length; p++) {
		    		if (indexes.get(p).get(pointers[p]) == min){ pointers[p] += 1; }
		    	}   

		    }
		}
		
		return resultRecords;
	}
	
	/*
	 * Return an index
	 */
	private List<Integer> getIndex(Map<String, List<Integer>> index, String value) {
		if(value == null)  { return null; }
		return index.getOrDefault(value, null);
	}

	
	
}
