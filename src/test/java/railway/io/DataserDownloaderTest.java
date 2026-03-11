package railway.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DataserDownloaderTest {
	
	final static String testUrl = "https://opendata.rijdendetreinen.nl/public/services/services-2026-01.csv.gz";
	final static String filename = testUrl.substring(testUrl.lastIndexOf('/') + 1);
	
	@Test
	@Disabled
	void fileIsCachedAfterFetch() throws Exception {

	    File cache = new File("data", filename);
	    
	    // force download it deleting the cache
	    if(cache.exists()) cache.delete();
	    
	    DatasetDownloader.fetch(testUrl);

	    assertTrue(cache.exists(), "File should exist in cache after request.");
	}
	
	@Test
	void fileIsCachedAfterRequest() throws Exception {

	    File cache = new File("src/main/resources", filename);

	    DatasetDownloader.fetch(testUrl);

	    assertTrue(cache.exists(), "File should exist in cache after request.");
	}


}
