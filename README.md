# Train Delay Analyzer

## Overview

**Train Delay Analyzer** is a Java-based project that fetches, parses, and analyzes railway delay data. It provides both a **command-line interface** for experimentation and a **Spring Boot REST API** for querying statistics on trains, stations, services, and companies.

The project demonstrates:

- Hands-on **Java development** and object-oriented design  
- **Clean coding** and **unit testing** practices  
- Exposure to **Spring Boot** and RESTful APIs  
- Basic memory optimization techniques (string deduplication)  

---

## Features

- Download and cache large CSV datasets from the official Dutch railway open data service  
- Parse the CSV into structured `TrainRecord` and `DelayRecord` objects  
- Compute statistics such as delays per train, station, service, and company  
- REST API endpoints to retrieve live reports:

```text
GET /delays/trains
GET /delays/trains/{trainNumber}
GET /delays/stations
GET /delays/stations/{stationName}
GET /delays/services
GET /delays/services/{serviceName}
GET /delays/companies
GET /delays/companies/{companyName}
POST /delays/custom  (with custom filters)
```

## Architecture

- DatasetDownloader: Fetches and caches CSV files
- CsvParser: Converts raw CSV lines into typed Java objects
- DataTable: Stores processed records in memory
- DelayAnalysisService: Computes statistics
- Controller: Exposes REST API endpoints
- Spring Boot Application: Initializes the service and preloads data at startup

## Setup

Clone the repository:
```code
git clone <repo_url>
cd train-delay-analyzer
```

### With Maven
Build with Maven:
```code
mvn clean install
```
Run the Spring Boot application:
```code
mvn spring-boot:run
```

### With Docker
Additionally, a docker container is prepared in the root to run the API.
Build with Docker:
```code
docker build -t train-delay-analyzer .
```

And run:
```code
docker run  -d -p 8080:8080 -t train-delay-analyzer
```

## Tests

Unit tests are implemented using JUnit 5. Run all tests with:
```console
mvn test
```
Tests cover CSV parsing, missing headers, invalid dates, and empty datasets.
Mock datasets are used to ensure tests run quickly without downloading large files.

## Notes / Limitations

- Memory usage can peak during CSV parsing due to dataset size (~5 GB raw, ~15 GB processed).
- String deduplication reduces memory usage but does not fully eliminate peak allocation.
- For real-time or production use, precomputed statistics or streaming processing would be recommended.

## Future Improvements

- Switch to streaming parsing to reduce peak memory usage
- Precompute statistics for faster API responses
- Support multiple years of data
- Add more sophisticated filters and aggregation options
