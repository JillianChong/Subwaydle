package src.main.java;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W'/*, 'Z' */};

    public static SubwayMap map = new SubwayMap();;

    public static void main(String[] args) {
        String path = generatePath();
        System.out.println(path);
    }

    public static char generateTrain(List<Character> possibleTrains, List<Character> currentTrains) {
        Random rand = new Random();

        List<Character> currentPossibilities = new ArrayList<>();
        for(Character train : possibleTrains) {
            if(!currentTrains.contains(train)) {
                currentPossibilities.add(train);
            }
        }

        if(currentPossibilities.isEmpty()) {
            return '0'; // no train can be generated
        }

        int num = rand.nextInt(currentPossibilities.size());

        return currentPossibilities.get(num);
    }

    public static String generatePoint(char train, String transferStation) {
        List<String> stations = map.getStations(train); // catch an exception?

        Random rand = new Random();
        
        int num = rand.nextInt(stations.size());
        while(transferStation.equals(stations.get(num))) {
            num = rand.nextInt(stations.size());
        }

        return stations.get(num);
    }

    public static String findTransferStation(char train, String previousTransfer, List<String> stationsSeen) {
        Random rand = new Random();

        List<String> possibleTransfers = map.getTransfers(train);
        // System.out.println(possibleTransfers);

        List<String> currentPossibilities = new ArrayList<>();
        for(int i = 0; i < possibleTransfers.size(); i++) {
            String consideringStation = possibleTransfers.get(i);
            if(!(stationsSeen.contains(consideringStation) || previousTransfer.equals(consideringStation))) {
                currentPossibilities.add(consideringStation);
            }
        }

        if(currentPossibilities.isEmpty()) {
            return "0"; // no transfer stations found
        }

        int num = rand.nextInt(currentPossibilities.size());

        return currentPossibilities.get(num);
    }

    public static List<String> addToRoute(char train, String start, String end) { 

        // TODO : MAKE EXCEPTION FOR STATION NOT FOUND -- add unit test for this
        List<String> trainLine = map.getStations(train);
        List<String> stationsSeen = new ArrayList<>();

        int startIndex = trainLine.indexOf(start);
        int endIndex = trainLine.indexOf(end);

        if(startIndex < endIndex) {
            for(int i = startIndex+1; i < endIndex; i++) {
                stationsSeen.add(trainLine.get(i));
            }
        } else {
            for(int i = startIndex-1; i > endIndex; i--) {
                stationsSeen.add(trainLine.get(i));
            }            
        }

        return stationsSeen;
    }

    public static String generatePath() { // make sure addRoutes is proper & includes transferStation
        Random rand = new Random();

        List<Character> currentTrains = new ArrayList<>();
        List<String> stationsPassed = new ArrayList<>();

        // generate Train Line 1
        char train1 = trains[rand.nextInt(trains.length)];
        currentTrains.add(train1);

        // generate Transfer 1
        String transferStation1 = findTransferStation(train1, "", stationsPassed);

        // generate start point
        String start = generatePoint(train1, transferStation1);

        // create current route
        stationsPassed.add(start);
        stationsPassed.addAll(addToRoute(train1, start, transferStation1));
        stationsPassed.add(transferStation1);
        // System.out.println("PASS 1: " + stationsPassed);

        // generate Train Line 2
        List<Character> possibleTrains2 = map.getTrains(transferStation1);
        char train2 = generateTrain(possibleTrains2, currentTrains);
        currentTrains.add(train2);

        // generate Tranfer 2
        String transferStation2 = findTransferStation(train2, transferStation1, stationsPassed);

        // add to current route
        stationsPassed.addAll(addToRoute(train2, transferStation1, transferStation2));
        stationsPassed.add(transferStation2);
        // System.out.println("PASS 2: " + stationsPassed);

        // generate Train Line 3
        List<Character> possibleTrains3 = map.getTrains(transferStation2);
        char train3 = generateTrain(possibleTrains3, currentTrains);
        currentTrains.add(train3);

        // generate end point
        String end = generatePoint(train3, transferStation2);

        // complete current route
        stationsPassed.addAll(addToRoute(train3, transferStation2, end));
        stationsPassed.add(end);
        // System.out.println("PASS 3: " + stationsPassed);

        return "START: " + start + " -> " + Character.toString(train1) + " -> " + transferStation1 + " -> " + 
                Character.toString(train2) + " -> " + transferStation2 + " -> " +
                Character.toString(train3) + " -> " + "END: " + end;
    }

    public static <K, V> void printHashMap(HashMap<K, V> map) { 
        for(Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " Train: " + entry.getValue());
            System.out.println();
        }
    }
}
