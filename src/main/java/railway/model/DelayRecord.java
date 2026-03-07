package railway.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DelayRecord {

    private LocalDate date;
    private String type;
    private String company;
    private String trainNumber;
    private Boolean completelyCancelled;
    private Boolean partlyCancelled;

    private String stationName;
    private LocalDateTime arrivalTime;
    private Integer arrivalDelay; 
    private LocalDateTime departureTime;
    private Integer departureDelay; 
    private Boolean departureCancelled;

    // Constructor
    public DelayRecord(LocalDate date, String type, String company, String trainNumber,
    		Boolean completelyCancelled, Boolean partlyCancelled, String stationName,
                       LocalDateTime arrivalTime, Integer arrivalDelay,
                       LocalDateTime departureTime, Integer departureDelay, Boolean departureCancelled) {
        this.date = date;
        this.type = type;
        this.company = company;
        this.trainNumber = trainNumber;
        this.completelyCancelled = completelyCancelled;
        this.partlyCancelled = partlyCancelled;
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.arrivalDelay = arrivalDelay;
        this.departureTime = departureTime;
        this.departureDelay = departureDelay;
        this.departureCancelled = departureCancelled;
    }

    // Getters
    public String getTrainNumber() { return trainNumber; }
    public String getStationName() { return stationName; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public LocalDateTime getDepartureTime() { return departureTime; }
	public String getService() { return type; }
	public String getCompany() { return company; }
	public LocalDate getServiceDate() { return date; }
	public boolean isServiceCompletetlyCancelled() { return completelyCancelled; }
	public boolean isServicePartiallyCancelled() { return partlyCancelled; }
	public boolean isStopCancelled() { return departureCancelled; }
	public boolean isScheduled() { return (departureTime != null); }
	public Integer getDelay() { return departureDelay != null? departureDelay: 0; }
	public boolean isDelayed() { return getDelay() > 0; }
	
    @Override
    public String toString() {
        return trainNumber + " @ " + stationName + " | Arrival: " + arrivalTime + " Delay: " + arrivalDelay +
               " | Departure: " + departureTime + " Delay: " + departureDelay;
    }











}
