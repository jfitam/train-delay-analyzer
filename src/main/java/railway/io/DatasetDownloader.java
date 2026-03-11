package railway.io;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.GZIPInputStream;

/**
 * Downloads railway datasets from a remote source.
 *
 * The downloader supports gzipped files and caches them locally
 * to avoid repeated downloads during development.
 */
public class DatasetDownloader {
	
	/**
	 * Reads the file in the url provided (or saved file cache, if exists).
	 * 
	 * @param urlString url in String format to read online
	 * @return List of the lines of the file
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static Queue<String> fetch(String urlString) throws MalformedURLException, IOException, URISyntaxException {
		String filename = urlString.substring(urlString.lastIndexOf('/') + 1);
		File cache = new File("src/main/resources", filename);
		InputStream input = null;

		if (cache.exists()) {
			input = new FileInputStream(cache);
		} else {
			input = DatasetDownloader.class.getClassLoader().getResourceAsStream(filename);
		}
		
		// download the file
		if (input == null) {
		    cache.getParentFile().mkdirs();
		    try (
		        InputStream downloadInput = new URI(urlString).toURL().openStream();
		        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cache))
		    ) {
		        URL urlObj = new URI(urlString).toURL();
		        URLConnection conn = urlObj.openConnection();
		        int totalBytes = conn.getContentLength();

		        byte[] buffer = new byte[8192];
		        int bytesRead;
		        int totalBytesRead = 0;
		        int prevPercent = 0;

		        while ((bytesRead = downloadInput.read(buffer)) != -1) {
		            bos.write(buffer, 0, bytesRead);
		            totalBytesRead += bytesRead;
		            int percentCompleted = (int) (totalBytesRead * 100L / totalBytes);
		            if (prevPercent != percentCompleted) {
		            	System.out.print("\rDownloading.. " + percentCompleted + "%");
		            }
		        }
		    }
		    System.out.println("\rDownload completed.");

		    input = new FileInputStream(cache);
		}
		
		//fetch data
	    Queue<String> rawData = new LinkedList<>();
		
	    System.out.println("Reading..");
	    
        try (
            GZIPInputStream gzip = new GZIPInputStream(input);
            InputStreamReader reader = new InputStreamReader(gzip);
            BufferedReader br = new BufferedReader(reader)
            ) {
			String newLine;
			while ((newLine = br.readLine()) != null) {
				rawData.add(newLine);
			}
		}
        
        System.out.println("Reading Completed.");
		
		return rawData;
	}
}
