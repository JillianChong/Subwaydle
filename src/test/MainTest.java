package src.test;

import org.junit.Test;

import src.main.java.Main;
import src.main.java.SubwayMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            for(String station : map.getTransfers(train)) {
                assertTrue(station + " does not exist for train " + train, map.getStations(train).contains(station));
            }
        }
    }

    @Test
    public void generateTrainTest() {
        List<Character> possibleTrains0 = new ArrayList<>();

        List<Character> currentTrains0 = new ArrayList<>();
        currentTrains0.add('J');
        assertEquals("Empty list test", '0', Main.generateTrain(possibleTrains0, currentTrains0));

        List<Character> possibleTrains1 = new ArrayList<>();
        Collections.addAll(possibleTrains1, 'A', 'B', 'C', '1', '2', '3', '7');
        
        List<Character> currentTrains1 = new ArrayList<>();
        currentTrains1.add('C');

        for(int i = 0; i < possibleTrains1.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains1, currentTrains1);
            // System.out.println("TRAIN: " + generatedTrain);
            assertFalse(generatedTrain == 'C');
        }

        Collections.addAll(currentTrains1, '1', 'B', '7');
        for(int i = 0; i < possibleTrains1.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains1, currentTrains1);
            // System.out.println("TRAIN " + i + ": " + generatedTrain);
            assertNotEquals("0 should not be generated", '0', generatedTrain);
            assertNotEquals("C should not be generated", 'C', generatedTrain);
            assertNotEquals("1 should not be generated", '1', generatedTrain);
            assertNotEquals("B should not be generated", 'B', generatedTrain);
            assertNotEquals("7 should not be generated", '7', generatedTrain);
        }

        List<Character> possibleTrains2 = new ArrayList<>();
        Collections.addAll(possibleTrains2, 'E', '2', 'G', 'L', '4', 'A');

        List<Character> currentTrains2 = new ArrayList<>();
        Collections.addAll(currentTrains2, '4', 'G', '2', 'L', 'A', 'E');

        assertEquals("Same list test", '0', Main.generateTrain(possibleTrains2, currentTrains2));

        char removedChar1 = currentTrains2.get(0);
        currentTrains2.remove(0);
        for(int i = 0; i < possibleTrains2.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains2, currentTrains2);
            assertEquals("Only one option test 1", removedChar1, generatedTrain);
        }

        char removedChar2 = currentTrains2.get(0);
        currentTrains2.remove(0);
        for(int i = 0; i < possibleTrains2.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains2, currentTrains2);
            assertTrue("Two option test", removedChar1 == generatedTrain || removedChar2 == generatedTrain);
        }

        List<Character> possibleTrains3 = new ArrayList<>();
        possibleTrains3.add('4');

        List<Character> currentTrains3 = new ArrayList<>();
        Collections.addAll(currentTrains3, 'B', '3', 'L', 'M', '5');
        assertEquals("One option test 2", '4', Main.generateTrain(possibleTrains3, currentTrains3));
        assertEquals("One option test 3", '4', Main.generateTrain(possibleTrains3, currentTrains3));

        currentTrains3.add('4');
        assertEquals("No train left test", '0', Main.generateTrain(possibleTrains3, currentTrains3));
    }

    @Test
    public void generatePointTest() {
        String generatedPoint1 = Main.generatePoint('F', "Parsons Blvd");
        assertTrue("Station not found test 1", map.getStations('F').contains(generatedPoint1));
        assertFalse("Station not found test 2", map.getStations('1').contains(generatedPoint1));
        assertNotEquals("Inequal stations test 1", generatedPoint1, "Parsons Blvd");

        String generatedPoint2 = Main.generatePoint('7', "61 St-Woodside");
        assertTrue("Station not found test 2", map.getStations('7').contains(generatedPoint2));
        assertFalse("Station not found test 4", map.getStations('J').contains(generatedPoint2));
        assertNotEquals("Inequal stations test 2", generatedPoint2, "61 St-Woodside");
    }

    @Test
    public void findTransferStationTest() {
        List<String> stationsSeen1 = new ArrayList<>();
        Collections.addAll(stationsSeen1, "Sutphin Blvd", "Briarwood", "Kew Gardens-Union Tpke", "75 Av", 
            "Forest Hills-71 Av", "Jackson Hts-Roosevelt Av & 74 St-Broadway", "21 St-Queensbridge");

        for(int i = 0; i < 20; i++) {
            String transferStation1 = Main.findTransferStation('F', "Queensboro Plaza", stationsSeen1);
            assertFalse(stationsSeen1.contains(transferStation1));
        }

        List<String> stationsSeen2 = new ArrayList<>();
        Collections.addAll(stationsSeen2, "Broadway Junction", "Brooklyn Bridge-City Hall & Chambers St (E)", 
            "Canal St (E)", "Delancey St-Essex St", "Fulton St", "Myrtle Av");
        String transferStation2 = Main.findTransferStation('J', "Fulton St", stationsSeen2);
        assertEquals("Only one station left", "Sutphin Blvd-Archer Av-JFK Airport", transferStation2);
        
        stationsSeen2.add("Sutphin Blvd-Archer Av-JFK Airport");
        transferStation2 = Main.findTransferStation('J', "Fulton St", stationsSeen2);
        assertEquals("No transfer stations found", "0", transferStation2);
    }

    @Test
    public void addToRouteTest() {
        List<String> route1 = Main.addToRoute('R', "65 St", "Lexington Av/59 St & Lexington Av/63 St & 59 St");

        List<String> expectedRoute1 = new ArrayList<>();
        Collections.addAll(expectedRoute1, "Northern Blvd", "46 St", "Steinway St", "36 St", "Queens Plaza");

        assertEquals(route1, expectedRoute1);

        String removed = expectedRoute1.get(0);
        expectedRoute1.remove(0);
        assertNotEquals("One station missing test", route1, expectedRoute1);

        expectedRoute1.add(removed);
        assertNotEquals("Stations out of order", route1, expectedRoute1);

        List<String> route2 = Main.addToRoute('G', "Bedford-Nostrand Avs", "Bedford-Nostrand Avs");

        assertTrue(route2.size() == 0);
        assertFalse(route2.contains("Bedford-Nostrand Avs"));
        assertFalse(route2.contains("Classon Av"));

        List<String> route3 = Main.addToRoute('6', "96 St", "Cypress Av");

        List<String> expectedRoute3 = new ArrayList<>();
        Collections.addAll(expectedRoute3, "103 St", "110 St", "116 St", "125 St", "3 Av-138 St", "Brook Av");

        assertEquals("Size test", 6, route3.size());
        assertEquals("Reverse order test", route3, expectedRoute3);
        assertEquals("Order test 1", "103 St", route3.get(0));
        assertEquals("Order test 2", "Brook Av", route3.get(route3.size()-1));
    }
}
