package src.test;

import org.junit.Test;

import src.main.java.com.example.Main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    @Test
    public void sortByLinesTest() {
        Main.sortByLines();

        // checking number of stations on each line
        assertEquals(22, Main.linesByStation.size());
        assertEquals(38, Main.linesByStation.get('1').size());
        assertEquals(49, Main.linesByStation.get('2').size());
        assertEquals(34, Main.linesByStation.get('3').size());
        assertEquals(28, Main.linesByStation.get('4').size());
        assertEquals(45, Main.linesByStation.get('5').size());
        assertEquals(38, Main.linesByStation.get('6').size());
        assertEquals(23, Main.linesByStation.get('7').size());
        assertEquals(45, Main.linesByStation.get('A').size());
        assertEquals(37, Main.linesByStation.get('B').size());
        assertEquals(40, Main.linesByStation.get('C').size());
        assertEquals(36, Main.linesByStation.get('D').size());
        assertEquals(22, Main.linesByStation.get('E').size());
        assertEquals(45, Main.linesByStation.get('F').size());
        assertEquals(21, Main.linesByStation.get('G').size());
        assertEquals(29, Main.linesByStation.get('J').size());
        assertEquals(24, Main.linesByStation.get('L').size());
        assertEquals(36, Main.linesByStation.get('M').size());
        assertEquals(28, Main.linesByStation.get('N').size());
        assertEquals(29, Main.linesByStation.get('Q').size());
        assertEquals(45, Main.linesByStation.get('R').size());
        assertEquals(23, Main.linesByStation.get('W').size());
        assertEquals(21, Main.linesByStation.get('Z').size());

        // verify certain station names at designated lines
    }

    @Test
    public void sortByTransfersTest() {
        Main.sortByTransfers();

        // checking hashmap contains all stations
        assertEquals(23, Main.transfersByLine.size());
        assertTrue(Main.transfersByLine.containsKey('S'));
        
        // checking specific stations
        assertEquals(15, Main.transfersByLine.get('F').size());
        assertTrue(Main.transfersByLine.get('E').contains("Chambers St (M) & World Trade Center & Park Place & Cortlandt St"));
        assertFalse(Main.transfersByLine.get('E').contains("World Trade Center"));
        assertEquals(13, Main.transfersByLine.get('2').size());
    } 

    @Test
    public void sortTransfersTest() {
        Main.sortTransfers();

        // checking size of hashmap
        assertEquals(61, Main.transfersByStation.size());
        assertEquals(3, Main.transfersByStation.get("4 Av & 4 Av-9 Sts").size());
        assertEquals(7, Main.transfersByStation.get("Chambers St (M) & World Trade Center & Park Place & Cortlandt St").size());
        assertEquals(3, Main.transfersByStation.get("Queensboro Plaza").size());

        // checking specific stations
        assertTrue(Main.transfersByStation.containsKey("Court Sq-23rd St & Court Sq"));
    }

    @Test
    public void matchingTest() {
        Main.sortByLines();
        Main.sortByTransfers();
        Main.sortTransfers();

        assertTrue(Main.transfersByLine.get('E').contains("14 St (8 Av)"));
        assertTrue(Main.linesByStation.get('E').contains("14 St (8 Av)"));

        // verifying the transfer station names match
        for(char train : trains) {
            System.out.println("TRAIN: " + train);
            for(String station : Main.transfersByLine.get(train)) {
                System.out.println(station);
                assertTrue(Main.linesByStation.get(train).contains(station));
            }
        }

    }
}
