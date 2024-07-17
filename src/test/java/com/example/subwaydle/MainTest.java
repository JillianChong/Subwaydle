package com.example.subwaydle;

// import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

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
        assertEquals(22, map.getTransfersByLine().size());        
        // checking specific stations
        assertEquals(15, map.getTransfers('F').size());
        assertTrue(map.getTransfers('E').contains("Chambers St (M) & World Trade Center & Park Place & Cortlandt St"));
        assertFalse(map.getTransfers('E').contains("World Trade Center"));
        assertEquals(13, map.getTransfers('2').size());
    } 

    @Test
    public void sortTransfersTest() {
        // checking size of hashmap
        assertEquals(60, map.getTransfersByStation().size());
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
                assertTrue(map.getStations(train).contains(station), station + " does not exist for train " + train);
            }
        }
    }

    @Test
    public void generateTrainTest() {
        List<Character> possibleTrains0 = new ArrayList<>();

        List<Character> currentTrains0 = new ArrayList<>();
        currentTrains0.add('J');
        assertEquals('0', Main.generateTrain(possibleTrains0, currentTrains0), "Empty list test");

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
            assertNotEquals(generatedTrain, "0 should not be generated");
            assertNotEquals(generatedTrain, "C should not be generated");
            assertNotEquals(generatedTrain, "1 should not be generated");
            assertNotEquals(generatedTrain, "B should not be generated");
            assertNotEquals(generatedTrain, "7 should not be generated");
        }

        List<Character> possibleTrains2 = new ArrayList<>();
        Collections.addAll(possibleTrains2, 'E', '2', 'G', 'L', '4', 'A');

        List<Character> currentTrains2 = new ArrayList<>();
        Collections.addAll(currentTrains2, '4', 'G', '2', 'L', 'A', 'E');

        assertEquals('0', Main.generateTrain(possibleTrains2, currentTrains2), "Same list test");

        char removedChar1 = currentTrains2.get(0);
        currentTrains2.remove(0);
        for(int i = 0; i < possibleTrains2.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains2, currentTrains2);
            assertEquals(removedChar1, generatedTrain, "Only one option test 1");
        }

        char removedChar2 = currentTrains2.get(0);
        currentTrains2.remove(0);
        for(int i = 0; i < possibleTrains2.size()*2; i++) {
            char generatedTrain = Main.generateTrain(possibleTrains2, currentTrains2);
            assertTrue(removedChar1 == generatedTrain || removedChar2 == generatedTrain, "Two option test");
        }

        List<Character> possibleTrains3 = new ArrayList<>();
        possibleTrains3.add('4');

        List<Character> currentTrains3 = new ArrayList<>();
        Collections.addAll(currentTrains3, 'B', '3', 'L', 'M', '5');
        assertEquals('4', Main.generateTrain(possibleTrains3, currentTrains3), "One option test 2");
        assertEquals('4', Main.generateTrain(possibleTrains3, currentTrains3), "One option test 3");

        currentTrains3.add('4');
        assertEquals('0', Main.generateTrain(possibleTrains3, currentTrains3), "No train left test");
    }

    @Test
    public void generatePointTest() {
        String generatedPoint1 = Main.generatePoint('F', "Parsons Blvd");
        assertTrue(map.getStations('F').contains(generatedPoint1), "Station not found test 1");
        assertFalse(map.getStations('1').contains(generatedPoint1), "Station not found test 2");
        assertNotEquals(generatedPoint1, "Parsons Blvd", "Inequal stations test 1");

        String generatedPoint2 = Main.generatePoint('7', "61 St-Woodside");
        assertTrue(map.getStations('7').contains(generatedPoint2), "Station not found test 2");
        assertFalse(map.getStations('J').contains(generatedPoint2), "Station not found test 4");
        assertNotEquals(generatedPoint2, "61 St-Woodside", "Inequal stations test 2");
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
        assertEquals("Sutphin Blvd-Archer Av-JFK Airport", transferStation2, "Only one station left");
        
        stationsSeen2.add("Sutphin Blvd-Archer Av-JFK Airport");
        transferStation2 = Main.findTransferStation('J', "Fulton St", stationsSeen2);
        assertEquals("0", transferStation2, "No transfer stations found");
    }

    @Test
    public void checkTransferStationTest() {
        List<String> stationsSeen1 = new ArrayList<>();
        Collections.addAll(stationsSeen1, "Sutphin Blvd", "Briarwood", "Kew Gardens-Union Tpke", "75 Av", 
            "Forest Hills-71 Av", "Jackson Hts-Roosevelt Av & 74 St-Broadway", "21 St-Queensbridge");

        List<String> newRoute1 = new ArrayList<>();
        Collections.addAll(newRoute1, "21 St-Queensbridge", "Roosevelt Island");

        assertFalse(Main.checkTransferStation(stationsSeen1, newRoute1));

        List<String> stationsSeen2 = new ArrayList<>();
        List<String> newRoute2 = new ArrayList<>();
        Collections.addAll(newRoute2, "Gun Hill Rd", "Pelham Pkwy", "Morris Park");
        
        assertTrue(Main.checkTransferStation(stationsSeen2, newRoute2), "Empty list test");
    }

    @Test
    public void addToRouteTest() {
        List<String> route1 = Main.addToRoute('R', "65 St", "Lexington Av/59 St & Lexington Av/63 St & 59 St");

        List<String> expectedRoute1 = new ArrayList<>();
        Collections.addAll(expectedRoute1, "Northern Blvd", "46 St", "Steinway St", "36 St", "Queens Plaza");

        assertEquals(route1, expectedRoute1);

        String removed = expectedRoute1.get(0);
        expectedRoute1.remove(0);
        assertNotEquals(route1, expectedRoute1, "One station missing test");

        expectedRoute1.add(removed);
        assertNotEquals(route1, expectedRoute1, "Stations out of order");

        List<String> route2 = Main.addToRoute('G', "Bedford-Nostrand Avs", "Bedford-Nostrand Avs");

        assertTrue(route2.size() == 0);
        assertFalse(route2.contains("Bedford-Nostrand Avs"));
        assertFalse(route2.contains("Classon Av"));

        List<String> route3 = Main.addToRoute('6', "96 St", "Cypress Av");

        List<String> expectedRoute3 = new ArrayList<>();
        Collections.addAll(expectedRoute3, "103 St", "110 St", "116 St", "125 St", "3 Av-138 St", "Brook Av");

        assertEquals(6, route3.size(), "Size test");
        assertEquals(route3, expectedRoute3, "Reverse order test");
        assertEquals("103 St", route3.get(0), "Order test 1");
        assertEquals("Brook Av", route3.get(route3.size()-1), "Order test 2");
    }

    @Test
    public void generatePathTest() {
        String[] path1 = Main.generatePath();
        char train1 = path1[0].charAt(0);
        char train2 = path1[1].charAt(0);
        char train3 = path1[2].charAt(0);
        assertTrue(train1 != train2 && train2 != train3 && train1 != train3, "Unique trains");
        assertTrue(map.getTrains(path1[4]).contains(train1) && map.getTrains(path1[4]).contains(train2), "Transfer station 1 correct");
        assertTrue(map.getTrains(path1[5]).contains(train2) && map.getTrains(path1[5]).contains(train3), "Transfer station 2 correct");
        assertTrue(map.getStations(train1).contains(path1[4]), "Check start");
        assertTrue(map.getStations(train3).contains(path1[6]), "Check end");
    }

    @Test
    public void checkSingleTrainTest() {
        // TODO: Add tests
    }
    
    @Test
    public void checkAnswerTest() {
        // TODO: Add tests
    }

    @Test
    public void endGameTest() {
        // TODO: Add tests
    }
}
