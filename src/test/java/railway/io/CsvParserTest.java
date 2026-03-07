package railway.io;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import railway.model.DelayRecord;

class CsvParserTest {
	
	final static String HEADER = "Service:RDT-ID,Service:Date,Service:Type,Service:Company,Service:Train number,Service:Completely cancelled,Service:Partly cancelled,Service:Maximum delay,Stop:RDT-ID,Stop:Station code,Stop:Station name,Stop:Arrival time,Stop:Arrival delay,Stop:Arrival cancelled,Stop:Departure time,Stop:Departure delay,Stop:Departure cancelled,Stop:Platform change,Stop:Planned platform,Stop:Actual platform";
	mockConsumer handler;
	
	class mockConsumer implements Consumer<DelayRecord> {	
		List<DelayRecord> records;
		
		public mockConsumer() {
			records = new ArrayList<>();
		}
		@Override
		public void accept(DelayRecord r) {
			if(r != null) { records.add(r); };		
		}
		
		List<DelayRecord> getList(){
			return records;
		}
	}
	
	@BeforeEach
	void init() {
		handler = new mockConsumer();
	}
	
	@Test
	void parseEmptyDataset() {
		Queue<String> emptyDataset = new LinkedList<>();
		CsvParser.parse(emptyDataset, handler);
		assertTrue(handler.getList().isEmpty(), "Empty list should be provided when empty dataset.");
	}
	
	@Test
	void parseTwoRecords() {
		Queue<String> mockDS = new LinkedList<>(
				List.of(
						HEADER,
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,0,false,false,,"
						)
				);
		CsvParser.parse(mockDS, handler);
		List<DelayRecord> parsed = handler.getList();
		
		assertEquals(2, parsed.size(), "The number of elements does not match.");
		assertEquals("420", parsed.get(0).getTrainNumber(), "Train Number does not match.");
		assertNull(parsed.get(0).getArrivalTime());
		assertEquals(LocalDateTime.of(2026,1,1,2,1,0,0), parsed.get(0).getDepartureTime());
	}
	
	@Test
	void parseWithEmptyLine() {
		Queue<String> mockDS = new LinkedList<>(
				List.of(
						HEADER,
						"",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,0,false,false,,"
						)
				);
		
		CsvParser.parse(mockDS, handler);
		List<DelayRecord> parsed = handler.getList();
		
		assertEquals(2, parsed.size(), "Empty lines should be ignored.");
		
	}
	
	@Test
	void parseMissingHeaders() {
		Queue<String> mockDS = new LinkedList<>(
				List.of(
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,0,false,false,,"
						)
				);
		
		assertThrows(IllegalArgumentException.class, () -> CsvParser.parse(mockDS, handler));
	}
	
	@Test
	void parseWrongDate() {
		Queue<String> mockDS = new LinkedList<>(
				List.of(
						HEADER,
						"17489590,2026-13-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,0,false,false,,"
						)
				);
		assertThrows(DateTimeParseException.class, () -> CsvParser.parse(mockDS, handler));
	}
	
	@Test
	void parseMissingColums() {
		Queue<String> mockDS = new LinkedList<>(
				List.of(
						"Service:RDT-ID,Service:Date,Service:Type",
						"17489590,2026-13-01,Nightjet",
						"17489590,2026-01-01,Nightjet"
						)
				);
		assertThrows(IllegalArgumentException.class, () -> CsvParser.parse(mockDS, handler));
	}
	

}
