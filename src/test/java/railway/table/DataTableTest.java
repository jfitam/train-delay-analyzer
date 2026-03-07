package railway.table;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import railway.io.CsvParser;
import railway.model.DelayRecord;

class DataTableTest {

    private DataTable table;

    @BeforeEach
    void setUp() throws MalformedURLException, IOException, URISyntaxException {
        //create entries
        final String HEADER = "Service:RDT-ID,Service:Date,Service:Type,Service:Company,Service:Train number,Service:Completely cancelled,Service:Partly cancelled,Service:Maximum delay,Stop:RDT-ID,Stop:Station code,Stop:Station name,Stop:Arrival time,Stop:Arrival delay,Stop:Arrival cancelled,Stop:Departure time,Stop:Departure delay,Stop:Departure cancelled,Stop:Platform change,Stop:Planned platform,Stop:Actual platform";
		
        Queue<String> mockDS = new LinkedList<>(
				List.of(
						HEADER,
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
						"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,0,false,false,,",
						"17489591,2026-01-01,Intercity,NS Int,21404,false,false,5,158393396,ASD,Amsterdam Centraal,,,,2026-01-01T02:03:00+01:00,5,false,false,5a,5a\r\n",
						"17489591,2026-01-01,Intercity,NS Int,21404,false,false,0,158393397,ASS,Amsterdam Sloterdijk,2026-01-01T02:08:00+01:00,5,false,2026-01-01T02:08:00+01:00,5,false,false,7,7"
						)
				);
        
        //turn them to objects and add to the table
		table = new DataTable();
		CsvParser.parse(mockDS, table);

    }

    @Test
    void testGetAllReturnsAllRecords() {
        List<DelayRecord> all = table.getAll();
        assertEquals(4, all.size());
    }

    @Test
    void testGetByTrainNumber() {
        List<DelayRecord> result = table.get("21404", null, null, null);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getTrainNumber().equals("21404")));
    }

    @Test
    void testGetByStationName() {
        List<DelayRecord> result = table.get(null, "Würzburg Hbf", null, null);
        assertEquals(1, result.size());
        assertEquals("Würzburg Hbf", result.get(0).getStationName());
    }

    @Test
    void testGetByCompanyAndService() {
        List<DelayRecord> result = table.get(null, null, "Intercity", "NS Int");
        assertEquals(2, result.size());
        DelayRecord r = result.get(0);
        assertEquals("Intercity", r.getService());
        assertEquals("NS Int", r.getCompany());
    }

    @Test
    void testGetByMultipleFiltersNoMatch() {
        List<DelayRecord> result = table.get("420", "Amsterdam Centraal", null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByAllFilters() {
        List<DelayRecord> result = table.get("21404", "Amsterdam Centraal", "Intercity", "NS Int");
        assertEquals(1, result.size());
        DelayRecord r = result.get(0);
        assertEquals("21404", r.getTrainNumber());
        assertEquals("Amsterdam Centraal", r.getStationName());
        assertEquals("Intercity", r.getService());
        assertEquals("NS Int", r.getCompany());
    }

    @Test
    void testGetWithNullFiltersReturnsAll() {
        List<DelayRecord> result = table.get(null, null, null, null);
        assertEquals(4, result.size());
    }
}