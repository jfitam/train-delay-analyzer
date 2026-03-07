package railway.analysis;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import railway.io.CsvParser;
import railway.table.DataTable;

public class DelayAnalysisServiceTest {
	
	static Queue<String> mockData = new LinkedList<>(
			List.of("Service:RDT-ID,Service:Date,Service:Type,Service:Company,Service:Train number,Service:Completely cancelled,Service:Partly cancelled,Service:Maximum delay,Stop:RDT-ID,Stop:Station code,Stop:Station name,Stop:Arrival time,Stop:Arrival delay,Stop:Arrival cancelled,Stop:Departure time,Stop:Departure delay,Stop:Departure cancelled,Stop:Platform change,Stop:Planned platform,Stop:Actual platform\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,5,158393385,NURNB,Nürnberg Hbf,,,,2026-01-01T02:01:00+01:00,0,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393386,WURZB,Würzburg Hbf,2026-01-01T02:29:00+01:00,0,false,2026-01-01T02:31:00+01:00,1,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393387,FKW,Kassel-Wilhelmshöhe,2026-01-01T04:46:00+01:00,0,false,2026-01-01T04:48:00+01:00,1,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393388,HAMM,Hamm (Westf.),2026-01-01T06:28:00+01:00,0,false,2026-01-01T06:30:00+01:00,0,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393389,MUNST,Münster (Westf) Hbf,2026-01-01T06:51:00+01:00,0,false,2026-01-01T06:54:00+01:00,0,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393390,RHEINE,Rheine,2026-01-01T07:17:00+01:00,1,false,2026-01-01T07:19:00+01:00,5,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393391,BH,Bad Bentheim,2026-01-01T07:31:00+01:00,4,false,2026-01-01T07:34:00+01:00,2,false,false,,\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393392,ODZ,Oldenzaal,2026-01-01T07:44:00+01:00,2,false,2026-01-01T07:50:00+01:00,1,false,false,1,1\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393393,DV,Deventer,2026-01-01T08:34:00+01:00,0,false,2026-01-01T08:37:00+01:00,10,false,false,4,4\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393394,AMF,Amersfoort Centraal,2026-01-01T09:13:00+01:00,0,false,2026-01-01T09:16:00+01:00,100,false,false,5a,5a\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393394,AMF,Amersfoort Centraal,2026-01-01T09:13:00+01:00,0,false,2026-01-01T09:16:00+01:00,,true,false,5a,5a\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393394,AMF,Amersfoort Centraal,2026-01-01T09:13:00+01:00,0,false,2026-01-01T09:16:00+01:00,,true,false,5a,5a\r\n",
					"17489590,2026-01-01,Nightjet,NS Int,420,false,false,0,158393395,ASD,Amsterdam Centraal,2026-01-01T09:49:00+01:00,1,false,,,,true,15,15a\r\n",
					"17489591,2026-01-01,Intercity,NS,21404,false,false,5,158393396,ASD,Amsterdam Centraal,,,,2026-01-01T02:03:00+01:00,5,false,false,5a,5a\r\n",
					"17489591,2026-01-01,Intercity,NS,21404,false,false,0,158393397,ASS,Amsterdam Sloterdijk,2026-01-01T02:08:00+01:00,5,false,2026-01-01T02:08:00+01:00,5,false,false,7,7\r\n",
					"17489591,2026-01-01,Intercity,NS,21404,false,false,0,158393398,HLM,Haarlem,2026-01-01T02:19:00+01:00,3,false,,,,false,6a,6a\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,1,158393399,RTD,Rotterdam Centraal,,,,2026-01-01T02:05:00+01:00,0,false,false,7,7\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393400,DT,Delft,2026-01-01T02:15:00+01:00,0,false,2026-01-01T02:17:00+01:00,0,false,false,1,1\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393401,GV,Den Haag HS,2026-01-01T02:24:00+01:00,0,false,2026-01-01T02:26:00+01:00,0,false,false,6,6\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393402,LEDN,Leiden Centraal,2026-01-01T02:38:00+01:00,0,false,2026-01-01T02:41:00+01:00,0,false,false,5b,5b\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393403,SHL,Schiphol Airport,2026-01-01T02:57:00+01:00,0,false,2026-01-01T03:00:00+01:00,0,false,true,1/2,1\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393404,ASS,Amsterdam Sloterdijk,2026-01-01T03:09:00+01:00,0,false,2026-01-01T03:11:00+01:00,0,false,false,12,12\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393405,ASD,Amsterdam Centraal,2026-01-01T03:17:00+01:00,0,false,2026-01-01T03:20:00+01:00,0,false,false,4,4\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393406,ASB,Amsterdam Bijlmer ArenA,2026-01-01T03:30:00+01:00,1,false,2026-01-01T03:32:00+01:00,0,false,false,8,8\r\n",
					"17489592,2026-01-01,Intercity,NS,1410,false,false,0,158393407,UT,Utrecht Centraal,2026-01-01T03:50:00+01:00,0,false,,,,false,15,15\r\n",
					"17489593,2026-01-01,Snelbus ipv trein,NS,900507,false,false,0,158393408,DT,Delft,,,,2026-01-01T02:08:00+01:00,0,false,false,,\r\n",
					"17489593,2026-01-01,Snelbus ipv trein,NS,900507,false,false,0,158393409,RTD,Rotterdam Centraal,2026-01-01T02:30:00+01:00,0,false,,,,false,,\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,1,158393410,UT,Utrecht Centraal,,,,2026-01-01T02:15:00+01:00,0,false,false,7,7\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393411,ASB,Amsterdam Bijlmer ArenA,2026-01-01T02:30:00+01:00,1,false,2026-01-01T02:32:00+01:00,0,false,false,1,1\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393412,ASD,Amsterdam Centraal,2026-01-01T02:43:00+01:00,0,false,2026-01-01T02:46:00+01:00,0,false,false,8,8\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393413,ASS,Amsterdam Sloterdijk,2026-01-01T02:52:00+01:00,0,false,2026-01-01T02:54:00+01:00,0,false,false,11,11\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393414,SHL,Schiphol Airport,2026-01-01T03:04:00+01:00,0,false,2026-01-01T03:06:00+01:00,0,false,true,5/6,6\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393415,LEDN,Leiden Centraal,2026-01-01T03:22:00+01:00,0,false,2026-01-01T03:24:00+01:00,0,false,false,8b,8b\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393416,GV,Den Haag HS,2026-01-01T03:36:00+01:00,0,false,2026-01-01T03:39:00+01:00,0,false,false,4,4\r\n",
					"17489594,2026-01-01,Intercity,NS,1409,false,false,0,158393417,DT,Delft,2026-01-01T03:46:00+01:00,0,false,2026-01-01T03:48:00+01:00,0,false,false,4,4"));
	
	static DataTable storage;
	static DelayAnalysisService service;
	
	@BeforeAll
	static void setup() {
		storage = new DataTable();
		CsvParser.parse(mockData, storage);
		service = new DelayAnalysisService(storage);
	}
	
   @Test
    void shouldAggregateByTrain() {
        AnalysisResult result = service.getAllTrains();

        assertNotNull(result);
        assertEquals("Train Service", result.aggregationType()); 
        assertFalse(result.stats().isEmpty());
    }

    @Test
    void shouldReturnSingleTrain() {
        AnalysisResult result = service.getTrain("420");

        assertEquals(1, result.stats().size());
        assertEquals("420", result.stats().get(0).ref());
        assertEquals(0, result.stats().get(0).minimumDelay());
        assertEquals(0, result.stats().get(0).lowerQuartileDelay());
        assertEquals(1, result.stats().get(0).medianDelay());
        assertEquals(2, result.stats().get(0).upperQuartileDelay());
        assertEquals(100, result.stats().get(0).maximumDelay());
        assertEquals(2, result.stats().get(0).numStopsCancelled());
        assertEquals(70.0, result.stats().get(0).percentageDelay());
    }

    @Test
    void shouldAggregateStations() {
        AnalysisResult result = service.getAllStations();

        assertNotNull(result);
        assertFalse(result.stats().isEmpty());
    }

    @Test
    void shouldReturnSpecificStation() {
        AnalysisResult result = service.getStation("Amsterdam Centraal");

        assertEquals(1, result.stats().size());
        assertEquals("Amsterdam Centraal", result.stats().get(0).ref());
    }
	
}
