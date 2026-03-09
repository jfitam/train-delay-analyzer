package railway.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DelayRecord {

    private LocalDate date;
    private String type;
    private String company;
    private String trainNumber;
    private String stationName;
    private LocalDateTime departureTime;
    private Integer departureDelay; 
    private Boolean departureCancelled;
    
	Map<String,String> stringPool = new HashMap<>();

    // Constructor
    public DelayRecord(LocalDate date, String type, String company, String trainNumber, String stationName,
                       LocalDateTime departureTime, Integer departureDelay, Boolean departureCancelled) {
        this.date = date;
        this.type = dedup(type);
        this.company = dedup(company);
        this.trainNumber = dedup(trainNumber);
        this.stationName = dedup(stationName);
        this.departureTime = departureTime;
        this.departureDelay = departureDelay;
        this.departureCancelled = departureCancelled;
    }

    // Getters
    public String getTrainNumber() { return trainNumber; }
    public String getStationName() { return stationName; }
    public LocalDateTime getDepartureTime() { return departureTime; }
	public String getService() { return type; }
	public String getCompany() { return company; }
	public LocalDate getServiceDate() { return date; }
	public boolean isStopCancelled() { return departureCancelled; }
	public boolean isScheduled() { return (departureTime != null); }
	public Integer getDelay() { return departureDelay != null? departureDelay: 0; }
	public boolean isDelayed() { return getDelay() > 0; }
	
    @Override
    public String toString() {
        return trainNumber + " @ " + stationName + " | Departure: " + departureTime + " Delay: " + departureDelay;
    }
    
	/*
	 * Check for same string in pool and point to the same object
	 */
	
	String dedup(String s) {
	    String existing = stringPool.putIfAbsent(s, s);
	    return existing == null? s: existing;
	}











}
