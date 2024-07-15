package src.main.java.com.example;

import java.util.List;
import java.util.Random;

import src.main.java.SubwayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    public static List<String> stationsPassed = new ArrayList<>();

    public static SubwayMap map = new SubwayMap();

    public static void main(String[] args) {
        String path = generatePath();
        System.out.println(path);
    }

    public static String findTransferStation(char train, String previousTransfer) {
        Random rand = new Random();

        List<String> possibleTransfers = map.getTransfers(train);
        System.out.println(possibleTransfers);

        int num = rand.nextInt(possibleTransfers.size());
        String transferStation = possibleTransfers.get(num);

        while(stationsPassed.contains(transferStation) || previousTransfer.equals(transferStation)) {
            num = rand.nextInt(possibleTransfers.size());
        }

        return transferStation;
    }

    public static char generateTrain(List<Character> possibleTrains, char[] currentTrains) {
        Random rand = new Random(); // TODO: Is this actually randomizing?

        int num = rand.nextInt(possibleTrains.size());
        while(Arrays.binarySearch(currentTrains, (char)possibleTrains.get(num)) > -1) {
            num = rand.nextInt(possibleTrains.size());
        }

        return (char)possibleTrains.get(num);
    }

    public static String generatePoint(char train, String transferStation) {
        List<String> stations = map.getStations(train);

        Random rand = new Random();
        
        int num = rand.nextInt(stations.size());
        while(transferStation.equals(stations.get(num))) {
            num = rand.nextInt(stations.size());
        }

        return stations.get(num);
    }

    public static void addToRoute(List<String> trainLine, String start, String end) {
        int startIndex = trainLine.indexOf(start);
        int endIndex = trainLine.indexOf(end);

        if(startIndex < endIndex) {
            for(int i = startIndex; i <= endIndex; i++) {
                stationsPassed.add(trainLine.get(i));
            }
        } else {
            for(int i = startIndex; i >= endIndex; i--) {
                stationsPassed.add(trainLine.get(i));
            }            
        }
    }

    public static String generatePath() {
        Random rand = new Random();

        // generate Train Line 1
        char train1 = trains[rand.nextInt(trains.length)];
        System.out.println("TRAIN 1: " + train1);

        // generate Transfer 1
        String transferStation1 = findTransferStation(train1, "");
        System.out.println("TRANFER 1: " + transferStation1);

        // generate start point
        String start = generatePoint(train1, transferStation1);
        System.out.println("START: " + start);

        List<String> train1Line = map.getStations(train1);

        // create currentRoute
        addToRoute(train1Line, start, transferStation1);

        // generate Train Line 2
        List<Character> possibleTrains2 = map.getTrains(transferStation1);
        char train2 = generateTrain(possibleTrains2, new char[]{train1});
        System.out.println("TRAIN 2: " + train2);

        // generate Tranfer 2
        String transferStation2 = findTransferStation(train2, transferStation1);
        System.out.println("TRANSFER 2: " + transferStation2);

        List<String> train2Line = map.getStations(train2);

        // create currentRouteß
        addToRoute(train2Line, transferStation1, transferStation2);

        // generate Train Line 3
        List<Character> possibleTrains3 = map.getTrains(transferStation2);

        char train3 = generateTrain(possibleTrains3, new char[]{train1, train2});
        System.out.println("TRAIN 3: " + train3);

        // generate end point
        String end = generatePoint(train3, transferStation2);
        System.out.println("END: " + end);

        return start + "->" + Character.toString(train1) + "->" + transferStation1 + "->" + 
                Character.toString(train2) + "->" + transferStation2 + "->" +
                Character.toString(train3) + "->" + end;
    }

    public static <K, V> void printHashMap(HashMap<K, V> map) { 
        for(Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " Train: " + entry.getValue());
            System.out.println();
        }
    }
}
