package com.example.subwaydle;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    private static SubwayMap map = new SubwayMap();

    public static List<List<Character>> similarRoutes = new ArrayList<>();

    public static void main(String[] args) {

        /* FOR COMPLETELY TEXT-BASED GAME */

        // Generate & print path
        // String[] pathInfo = generatePath();
        // String path = printPath(pathInfo);
        // System.out.println(path);

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
            similarRoutes = new ArrayList<>();

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

            similarRoutes.add(findSimilarRoutes(train1, start, transferStation1));
            similarRoutes.add(findSimilarRoutes(train2, transferStation1, transferStation2));
            similarRoutes.add(findSimilarRoutes(train3, transferStation2, end));

            System.out.println("Similar Routes 1: " + similarRoutes.get(0));
            System.out.println("Similar Routes 2: " + similarRoutes.get(1));
            System.out.println("Similar Routes 3: " + similarRoutes.get(2));

            return output;
        }
    }

    public static List<Character> findSimilarRoutes(char train, String start, String end) {

        //First, check both trains have same start and end
        List<Character> linesAtStart = map.getTrains(start);
        List<Character> linesAtEnd = map.getTrains(end);
        List<Character> similar = new ArrayList<>();

        if(linesAtStart == null && linesAtEnd == null) {
            return new ArrayList<>();
        }

        if(linesAtStart != null) {
            for(Character c : linesAtStart) {
                if(c == train) {
                    continue;
                }
    
                if(!similar.contains(c)) {
                    similar.add(c);
                }
            }
        }

        if(linesAtEnd != null) {
            for(Character c : linesAtEnd) {
                if(c == train) {
                    continue;
                }
    
                if(!similar.contains(c)) {
                    similar.add(c);
                }
            }
        }

        // Second, check the path matches exactly
        List<String> trainRoute = map.getStations(train);
        int startIndex = trainRoute.indexOf(start);
        int endIndex = trainRoute.indexOf(end);

        if(endIndex > startIndex) {
            trainRoute = trainRoute.subList(startIndex, endIndex);
        } else {
            trainRoute = trainRoute.subList(endIndex, startIndex);
        }

        List<Character> finalSimilar = new ArrayList<>();
        for(Character similarTrain : similar) {
            List<String> similarRoute = map.getStations(similarTrain);

            startIndex = similarRoute.indexOf(start);
            endIndex = similarRoute.indexOf(end);

            if(startIndex == -1 || endIndex == -1) {
                continue;
            }

            if(endIndex > startIndex) {
                similarRoute = similarRoute.subList(startIndex, endIndex);
            } else {
                similarRoute = similarRoute.subList(endIndex, startIndex);
            }

            List<String> smaller = trainRoute.size() < similarRoute.size() ? trainRoute : similarRoute;
            List<String> larger = trainRoute.size() < similarRoute.size() ? similarRoute : trainRoute;
            boolean add = true;
            for(int i = 0; i < smaller.size(); i++) {
                if(!larger.contains(smaller.get(i))) {
                    add = false;
                    break;
                }
            }

            if(add) {
                finalSimilar.add(similarTrain);
            }
        }

        return finalSimilar;
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
