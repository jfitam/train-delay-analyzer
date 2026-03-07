package railway.io;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Queue;
import java.util.function.Consumer;

import railway.model.DelayRecord;

/**
 * Utility class responsible for parsing the railway delay CSV dataset.
 *
 * It reads raw CSV lines, detects column positions from the header,
 * converts fields to their corresponding Java types and produces
 * DelayRecord objects.
 */
public class CsvParser {
	
    // Service fields
    public static final String SERVICE_RDT_ID = "Service:RDT-ID";
    public static final String SERVICE_DATE = "Service:Date";
    public static final String SERVICE_TYPE = "Service:Type";
    public static final String SERVICE_COMPANY = "Service:Company";
    public static final String SERVICE_TRAIN_NUMBER = "Service:Train number";
    public static final String SERVICE_COMPLETELY_CANCELLED = "Service:Completely cancelled";
    public static final String SERVICE_PARTLY_CANCELLED = "Service:Partly cancelled";
    public static final String SERVICE_MAXIMUM_DELAY = "Service:Maximum delay";

    // Stop fields
    public static final String STOP_RDT_ID = "Stop:RDT-ID";
    public static final String STOP_STATION_CODE = "Stop:Station code";
    public static final String STOP_STATION_NAME = "Stop:Station name";
    public static final String STOP_ARRIVAL_TIME = "Stop:Arrival time";
    public static final String STOP_ARRIVAL_DELAY = "Stop:Arrival delay";
    public static final String STOP_ARRIVAL_CANCELLED = "Stop:Arrival cancelled";
    public static final String STOP_DEPARTURE_TIME = "Stop:Departure time";
    public static final String STOP_DEPARTURE_DELAY = "Stop:Departure delay";
    public static final String STOP_DEPARTURE_CANCELLED = "Stop:Departure cancelled";
    public static final String STOP_PLATFORM_CHANGE = "Stop:Platform change";
    public static final String STOP_PLANNED_PLATFORM = "Stop:Planned platform";
    public static final String STOP_ACTUAL_PLATFORM = "Stop:Actual platform";
    
    
    /**
     * Parses raw CSV data into DelayRecord objects.
     * The input queue is consumed during parsing.
     *
     * @param rawData queue containing the raw CSV lines
     * @param handler function to be executed after parsing every line.
     * @return list of parsed DelayRecord objects
     */
	public static void parse(Queue<String> rawData, Consumer<DelayRecord> handler) {
		if (rawData.isEmpty()) return;
		
		//headers map
		HashMap<String, Integer> headerIndex = new HashMap<>();
		String[] headers = getLine(rawData);
		
		for(int i = 0; i < headers.length; i++) {
			headerIndex.put(headers[i], i);
		}
		
		checkCorrectHeaders(headerIndex);
		
		
		//process
		String[] line;
		while ((line = getLine(rawData)) != null) {
			if (line.length == 0 ) continue;
			handler.accept(parseLine(headerIndex, line)); 
		}


	}
	
	private static String[] getLine(Queue<String> rawData) {
		String next = rawData.poll();
		
		if(next == null) return null;
		if(next.isEmpty()) return new String[0];

		String[] parts = next.replaceAll("(\\r|\\n)", "").split(",", -1);
		return parts;

	}

	private static void checkCorrectHeaders(HashMap<String, Integer> headerIndex) {
	    String[] required = {
	        SERVICE_DATE, SERVICE_TYPE, SERVICE_COMPANY,
	        SERVICE_TRAIN_NUMBER, SERVICE_COMPLETELY_CANCELLED, SERVICE_PARTLY_CANCELLED,
	        SERVICE_MAXIMUM_DELAY, STOP_STATION_NAME,
	        STOP_ARRIVAL_TIME, STOP_ARRIVAL_DELAY,
	        STOP_DEPARTURE_TIME, STOP_DEPARTURE_DELAY, STOP_DEPARTURE_CANCELLED,
	        STOP_PLATFORM_CHANGE, STOP_PLANNED_PLATFORM, STOP_ACTUAL_PLATFORM
	    };

	    for(int i = 0; i < required.length; i++) {
	    	if(!headerIndex.containsKey(required[i])) {
				throw new IllegalArgumentException("Headers miss field '" + required[i] + "'");
	    	}
	    }
	}

	private static DelayRecord parseLine(HashMap<String, Integer> headerIndex, String[] nl) {
		return new DelayRecord(
			    parseDate(nl[headerIndex.get(SERVICE_DATE)]),
			    nl[headerIndex.get(SERVICE_TYPE)],
			    nl[headerIndex.get(SERVICE_COMPANY)],
			    nl[headerIndex.get(SERVICE_TRAIN_NUMBER)],
			    parseBoolean(nl[headerIndex.get(SERVICE_COMPLETELY_CANCELLED)]),
			    parseBoolean(nl[headerIndex.get(SERVICE_PARTLY_CANCELLED)]),
			    nl[headerIndex.get(STOP_STATION_NAME)],
			    parseDateTime(nl[headerIndex.get(STOP_ARRIVAL_TIME)]),
			    parseInt(nl[headerIndex.get(STOP_ARRIVAL_DELAY)]),
			    parseDateTime(nl[headerIndex.get(STOP_DEPARTURE_TIME)]),
			    parseInt(nl[headerIndex.get(STOP_DEPARTURE_DELAY)]),
			    parseBoolean(nl[headerIndex.get(STOP_DEPARTURE_CANCELLED)])
				);
	}

	private static LocalDate parseDate(String datetStr) {
        if(datetStr == null || datetStr.isEmpty()) return null;
        return LocalDate.parse(datetStr, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	private static LocalDateTime parseDateTime(String datetimeStr) {
        if(datetimeStr == null || datetimeStr.isEmpty()) return null;
        OffsetDateTime odt = OffsetDateTime.parse(datetimeStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return odt.toLocalDateTime();
    }

    private static Integer parseInt(String s) {
        return (s == null || s.isEmpty()) ? null : Integer.parseInt(s);
    }

    private static Boolean parseBoolean(String s) {
        return (s == null || s.isEmpty()) ? null : Boolean.parseBoolean(s);
    }

}
