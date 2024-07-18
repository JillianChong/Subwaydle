package com.example.subwaydle;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    public static SubwayMap map = new SubwayMap();;

    public static void main(String[] args) {

        /* FOR COMPLETELY TEXT-BASED GAME */

        // Generate & print path
        // String[] pathInfo = generatePath();
        // String path = printPath(pathInfo);
        // System.out.println(path);

        // // TODO: Implement keyboard box coloring
        // Checker check = new Checker(pathInfo[0], pathInfo[1], pathInfo[2]);
        // check.playGame();

        // System.out.println("YOU WIN! The route is: " + path);
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

    public static String generatePoint(char train, String transferStation) throws NullPointerException {
        try {
            List<String> stations = map.getStations(train); // catch an exception?

            Random rand = new Random();
            
            int num = rand.nextInt(stations.size());
            while(transferStation.equals(stations.get(num))) {
                num = rand.nextInt(stations.size());
            }

            return stations.get(num);
        } catch(NullPointerException e) {
            System.out.println("NULL POINTER: " + train + ", TRANSFER: " + transferStation);
        }
        
        return "";
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

    public static boolean checkTransferStation(List<String> stationsSeen, List<String> newRoute) {
        for(String station : newRoute) {
            if(stationsSeen.contains(station)) {
                return false;
            }
        }

        return true;
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

    public static String[] generatePath() { 
        // make sure addRoutes is proper & includes transferStation
        Random rand = new Random();

        while(true) {
            List<Character> currentTrains = new ArrayList<>();
            List<String> stationsPassed = new ArrayList<>();

            // generate Train Line 1
            char train1 = trains[rand.nextInt(trains.length)];
            currentTrains.add(train1);

            // generate Transfer 1
            String transferStation1 = findTransferStation(train1, "", stationsPassed);

            if(transferStation1.equals("0")) {
                continue;
            }

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

            if(transferStation2.equals("0")) {
                continue;
            }

            List<String> routeToAdd = addToRoute(train2, transferStation1, transferStation2);

            if(!checkTransferStation(stationsPassed, routeToAdd)) {
                continue;
            }

            // add to current route
            stationsPassed.addAll(routeToAdd);
            stationsPassed.add(transferStation2);
            // System.out.println("PASS 2: " + stationsPassed);

            // generate Train Line 3
            List<Character> possibleTrains3 = map.getTrains(transferStation2);
            char train3 = generateTrain(possibleTrains3, currentTrains);
            currentTrains.add(train3);

            // generate end point
            String end = generatePoint(train3, transferStation2);

            routeToAdd = addToRoute(train3, transferStation2, end);

            if(!checkTransferStation(stationsPassed, routeToAdd)) {
                continue;
            }

            // complete current route
            stationsPassed.addAll(routeToAdd);
            stationsPassed.add(end);

            String[] output = new String[]{Character.toString(train1), Character.toString(train2), Character.toString(train3), start, transferStation1, transferStation2, end};

            return output;
        }
        
    }

    public static String printPath(String[] pathInfo) {
        return "START: " + pathInfo[3] + " -> " + pathInfo[0] + " -> " + pathInfo[4] + " -> " + 
        pathInfo[1] + " -> " + pathInfo[5] + " -> " +
        pathInfo[2] + " -> " + "END: " + pathInfo[6];
    }

    public static <K, V> void printHashMap(HashMap<K, V> map) { 
        for(Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " Train: " + entry.getValue());
            System.out.println();
        }
    }
}
