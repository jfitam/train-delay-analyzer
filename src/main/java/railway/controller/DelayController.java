package railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import railway.analysis.AnalysisResult;
import railway.analysis.DelayAnalysisService;

@RestController
@RequestMapping("/delays")
class DelayController {
	
	DelayAnalysisService reporter; 
	
	@Autowired
	public DelayController(DelayAnalysisService reporter) {
		this.reporter = reporter;
	}
	
	@GetMapping("/trains")
	public AnalysisResult getAllTrainsReport() {
		return reporter.getAllTrains();
	}
	
	@GetMapping("/trains/{trainNumber}")
	public AnalysisResult getTrainReport(@PathVariable String trainNumber) {
		return reporter.getTrain(trainNumber);
	}
	
	@GetMapping("/stations")
	public AnalysisResult getAllStationsReport() {
		return reporter.getAllStations();
	}
	
	@GetMapping("/stations/{stationName}")
	public AnalysisResult getStationReport(@PathVariable String stationName) {
		return reporter.getStation(stationName);	
	}
	
	@GetMapping("/services")
	public AnalysisResult getAllServicesReport() {
		return reporter.getAllServices();
	}
	
	@GetMapping("/services/{serviceName}")
	public AnalysisResult getServiceReport(@PathVariable String serviceName) {
		return reporter.getServices(serviceName);
	}
	
	@GetMapping("/companies")
	public AnalysisResult getAllCompaniesReport() {
		return reporter.getAllCompanies();
	}
	
	@GetMapping("/companies/{companyName}")
	public AnalysisResult getCompanyReport(@PathVariable String companyName) {
		return reporter.getCompany(companyName);	
	}
	
}