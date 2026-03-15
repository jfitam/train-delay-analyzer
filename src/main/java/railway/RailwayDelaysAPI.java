package railway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Base EntryPoint for Spring Boot
 */
@SpringBootApplication
public class RailwayDelaysAPI {
	private RailwayDelaysAPI() {}
    public static void main(final String[] args) {
        SpringApplication.run(RailwayDelaysAPI.class, args);
    }
}
