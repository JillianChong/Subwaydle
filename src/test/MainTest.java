package src.test;

import org.junit.Test;

import src.main.java.Main;
import src.main.java.SubwayMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    public SubwayMap map = new SubwayMap();

    @Test
    public void sortByLinesTest() {
        // checking number of stations on each line
        assertEquals(22, map.getLinesByStation().size());
        assertEquals(38, map.getStations('1').size());
        assertEquals(49, map.getStations('2').size());
        assertEquals(34, map.getStations('3').size());
        assertEquals(28, map.getStations('4').size());
        assertEquals(45, map.getStations('5').size());
        assertEquals(38, map.getStations('6').size());
        assertEquals(23, map.getStations('7').size());
        assertEquals(45, map.getStations('A').size());
        assertEquals(37, map.getStations('B').size());
        assertEquals(40, map.getStations('C').size());
        assertEquals(36, map.getStations('D').size());
        assertEquals(22, map.getStations('E').size());
        assertEquals(45, map.getStations('F').size());
        assertEquals(21, map.getStations('G').size());
        assertEquals(29, map.getStations('J').size());
        assertEquals(24, map.getStations('L').size());
        assertEquals(36, map.getStations('M').size());
        assertEquals(28, map.getStations('N').size());
        assertEquals(29, map.getStations('Q').size());
        assertEquals(45, map.getStations('R').size());
        assertEquals(23, map.getStations('W').size());
        assertEquals(21, map.getStations('Z').size());
    }

    @Test
    public void sortByTransfersTest() {
        // checking hashmap contains all stations
        assertEquals(23, map.getTransfersByLine().size());
        assertTrue(map.getTransfersByLine().containsKey('S'));
        
        // checking specific stations
        assertEquals(15, map.getTransfers('F').size());
        assertTrue(map.getTransfers('E').contains("Chambers St (M) & World Trade Center & Park Place & Cortlandt St"));
        assertFalse(map.getTransfers('E').contains("World Trade Center"));
        assertEquals(13, map.getTransfers('2').size());
    } 

    @Test
    public void sortTransfersTest() {
        // checking size of hashmap
        assertEquals(61, map.getTransfersByStation().size());
        assertEquals(3, map.getTrains("4 Av & 4 Av-9 Sts").size());
        assertEquals(7, map.getTrains("Chambers St (M) & World Trade Center & Park Place & Cortlandt St").size());
        assertEquals(3, map.getTrains("Queensboro Plaza").size());

        // checking specific stations
        assertTrue(map.getTransfersByStation().containsKey("Court Sq-23rd St & Court Sq"));
    }

    @Test
    public void matchingTest() {
        assertTrue(map.getTransfers('E').contains("14 St (8 Av)"));
        assertTrue(map.getStations('E').contains("14 St (8 Av)"));

        // verifying the transfer station names match
        for(char train : trains) {
            // System.out.println("TRAIN: " + train);
            for(String station : map.getTransfers(train)) {
                // System.out.println(station);
                assertTrue(map.getStations(train).contains(station));
            }
        }
    }
}
