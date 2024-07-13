import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // String : train, List<String> : stations
    // Stores a list of stations for each line
    public static HashMap<Character, List<String>> linesByStation = new HashMap<>();
    // String : train line, List<String> : stations
    // Stores a list of transfer stations for each line
    public static HashMap<Character, List<String>> transfersByLine = new HashMap<>();
    // String : station name, List<Character> : list of lines available --> duplicate stations will be listed separately (deal with connections)
    // Stores a list of lines available at each transfer station
    public static HashMap<String, List<Character>> transfersByStation = new HashMap<>();

    public static char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    public static List<String> stationsPassed = new ArrayList<>();

    public static void main(String[] args) {
        sortByTransfers();
        sortByLines();
        sortTransfers();
        String path = generatePath();
        System.out.println(path);
        // printHashMap(linesByStation);
        // System.out.println(transfersByStation.size());
        // for(String str : transfersByStation.keySet()) {
        //     System.out.println(str + " " + transfersByStation.get(str));
        // }
    }

    public static void sortByLines() {
        File file;
        BufferedReader br;
        try {
            for(char train : trains) {
                String filePath = "./Stations/" + Character.toString(train) + "_train.txt";
                file = new File(filePath);

                br = new BufferedReader(new FileReader(file));
                br.readLine();

                List<String> stations = new ArrayList<>();

                String line = br.readLine();
                while(line != null) {
                    stations.add(line);
                    line = br.readLine();
                }

                linesByStation.put(train, stations);
            }
        } catch (FileNotFoundException e) {
            System.out.println("TRAIN FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    public static void sortByTransfers() {
        File file = new File("./Transfers/transfers_copy.txt");
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            while(line != null) {
                int index = line.indexOf("[");
                String stationName = line.substring(0, index-1);
                String trainList = line.substring(index+1, line.length()-1);
                trainList = trainList.replaceAll(", ", "");
                trainList = trainList.replaceAll("]", "");

                for(char train : trainList.toCharArray()) {
                    if(transfersByLine.containsKey(train)) {
                        List<String> stations = transfersByLine.get(train);
                        stations.add(stationName);
                    } else {
                        List<String> stations = new ArrayList<>();
                        stations.add(stationName);
                        transfersByLine.put(train, stations);
                    }
                }

                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("TRANSFERS FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    // TODO : COMBINE THIS WITH SORTBYTRANSFERS()
    public static void sortTransfers() {
        try {
            File file = new File("./Transfers/transfers_copy.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));  

            String line = br.readLine();
            while(line != null) {
                if(line.indexOf("&") > -1) {
                    int index = line.indexOf(" [");
                    String[] stations = line.substring(0, index).split("&");

                    List<Character> trainsAtStation = new ArrayList<>();
                    for(char ch : line.substring(index).toCharArray()) {
                        if(ch != ' ' && ch != ',' && ch != '[' && ch != ']') {
                            trainsAtStation.add(ch);
                        }
                    }

                    for(String station : stations) {
                        transfersByStation.put(station.strip(), trainsAtStation);
                    }
                } else {
                    int index = line.indexOf(" [");
                    String stationName = line.substring(0,index);

                    List<Character> trainsAtStation = new ArrayList<>();
                    for(char ch : line.substring(index).toCharArray()) {
                        if(ch != ' ' && ch != ',' && ch != '[' && ch != ']') {
                            trainsAtStation.add(ch);
                        }
                    }

                    transfersByStation.put(stationName, trainsAtStation);
                }

        
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("TRANSFERS FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    public static String findTransferStation(char train) {
        Random rand = new Random();

        List<String> possibleTransfers = transfersByLine.get(train);
        String transferStation = possibleTransfers.get(rand.nextInt(possibleTransfers.size()));

        if(transferStation.contains("&")) {
            transferStation = transferStation.substring(0,transferStation.indexOf('&')-1);
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
        List<String> stations = linesByStation.get(train);

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
        String transferStation1 = findTransferStation(train1);

        System.out.println("TRANSFER STATION 1: " + transferStation1);

        // generate start point
        String start = generatePoint(train1, transferStation1);

        System.out.println("START: " + start);
        List<String> train1Line = linesByStation.get(train1);

        System.out.println(train1Line.indexOf(transferStation1));

        // System.out.println("START: " + start + " -> " + train1Line);

        // create currentRoute
        // TODO : NEED TO FIX DUPLICATE NAMES, fix transferStation addedd twice?
        addToRoute(train1Line, start, transferStation1);

        // generate Train Line 2
        List<Character> possibleTrains2 = transfersByStation.get(transferStation1);
        // System.out.println("Station 1: " + transferStation1);
        // System.out.println("Possible Trains 2: " + possibleTrains2);
        // int num = rand.nextInt(possibleTrains2.size());
        // while(train1 == (char)possibleTrains2.get(num)) {
        //     num = rand.nextInt(possibleTrains2.size());
        // }

        // char train2 = possibleTrains2.get(num);
        char train2 = generateTrain(possibleTrains2, new char[]{train1});

        System.out.println("TRAIN 2: " + train2);

        // generate Transfer 2
        List<String> possibleTransfers2 = transfersByLine.get(train2);
        int num = rand.nextInt(possibleTransfers2.size());
        while(stationsPassed.contains(possibleTransfers2.get(num)) || transferStation1.equals(possibleTransfers2.get(num))) {
            num = rand.nextInt(possibleTransfers2.size());
        }

        String transferStation2 = possibleTransfers2.get(num);
        System.out.println("Station 2: " + transferStation2);

        if(transferStation2.contains("&")) {
            transferStation2 = transferStation2.substring(0,transferStation2.indexOf('&')-1);
        }

        List<String> train2Line = linesByStation.get(train2);

        // create currentRoute
        int startIndex = train2Line.indexOf(transferStation1); // TODO : NEED TO FIX DUPLICATE NAMES
        int transferIndex = train2Line.indexOf(transferStation2);

        if(startIndex < transferIndex) {
            for(int i = startIndex; i <= transferIndex; i++) {
                stationsPassed.add(train2Line.get(i));
            }
        } else {
            for(int i = startIndex; i >= transferIndex; i--) {
                stationsPassed.add(train2Line.get(i));
            }            
        }

        // generate Train Line 3
        List<Character> possibleTrains3 = transfersByStation.get(transferStation2);
        num = rand.nextInt(possibleTrains3.size());
        while(train1 == (char)possibleTrains3.get(num) || train2 == (char)possibleTrains3.get(num)) {
            num = rand.nextInt(possibleTrains3.size());
        }

        char train3 = possibleTrains3.get(num);

        // generate end point
        String end = generatePoint(train3, transferStation2);

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
