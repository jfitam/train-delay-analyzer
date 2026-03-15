package railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import railway.analysis.AnalysisResult;
import railway.analysis.DelayAnalysisService;

@RestController
@RequestMapping("/delays")
class DelayController {
	
	final private DelayAnalysisService reporter; 
	
	/**
	 * Constructor
	 * @param reporterUsed
	 */
	@Autowired
	public DelayController(DelayAnalysisService reporterUsed) {
		this.reporter = reporterUsed;
	}
	
	/**
	 * Returns JSON with the statistics for all trains
	 * @return AnalysisResult
	 */
	@GetMapping("/trains")
	public AnalysisResult getAllTrainsReport() {
		return reporter.getAllTrains();
	}
	
	/**
	 * Returns JSON with the statistics for specific train
	 * @return AnalysisResult
	 */
	@GetMapping("/trains/{trainNumber}")
	public AnalysisResult getTrainReport(@PathVariable String trainNumber) {
		return reporter.getTrain(trainNumber);
	}
	
	/**
	 * Returns JSON with the statistics for all stations
	 * @return AnalysisResult
	 */
	@GetMapping("/stations")
	public AnalysisResult getAllStationsReport() {
		return reporter.getAllStations();
	}
	
	/**
	 * Returns JSON with the statistics for specific station
	 * @return AnalysisResult
	 */
	@GetMapping("/stations/{stationName}")
	public AnalysisResult getStationReport(@PathVariable String stationName) {
		return reporter.getStation(stationName);	
	}
	
	/**
	 * Returns JSON with the statistics for all services
	 * @return AnalysisResult
	 */
	@GetMapping("/services")
	public AnalysisResult getAllServicesReport() {
		return reporter.getAllServices();
	}
	
	/**
	 * Returns JSON with the statistics for specific service
	 * @return AnalysisResult
	 */
	@GetMapping("/services/{serviceName}")
	public AnalysisResult getServiceReport(@PathVariable String serviceName) {
		return reporter.getServices(serviceName);
	}
	
	/**
	 * Returns JSON with the statistics for all companies
	 * @return AnalysisResult
	 */
	@GetMapping("/companies")
	public AnalysisResult getAllCompaniesReport() {
		return reporter.getAllCompanies();
	}
	
	/**
	 * Returns JSON with the statistics for specific company
	 * @return AnalysisResult
	 */
	@GetMapping("/companies/{companyName}")
	public AnalysisResult getCompanyReport(@PathVariable String companyName) {
		return reporter.getCompany(companyName);	
	}
	
}