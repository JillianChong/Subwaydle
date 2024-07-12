import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

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

    // TODO : figure out how to use abstraction for parameters
    public static void printHashMap(HashMap<Character, List<String>> map) { 
        for(Character train : map.keySet()) {
            // System.out.println(Character.toString(train) + " Train: " + printList(map.get(train)));
            System.out.println(Character.toString(train) + " Train: " + map.get(train));
            System.out.println();
        }
    }

    public static String printList(List<String> lst) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lst.size()-1; i++) {
            sb.append(lst.get(i) + ", ");
        }

        sb.append(lst.get(lst.size()-1));
        
        return sb.toString();
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

    public static String generatePath() {
        Random rand = new Random();

        // generate Train Line 1
        char train1 = trains[rand.nextInt(trains.length)];

        // generate Transfer 1
        List<String> possibleTransfers1 = transfersByLine.get(train1);
        String transferStation1 = possibleTransfers1.get(rand.nextInt(possibleTransfers1.size()));

        // generate Train Line 2
        List<Character> possibleTrains2 = transfersByStation.get(transferStation1);
        System.out.println("Station 1: " + transferStation1);
        int num = rand.nextInt(possibleTrains2.size());
        while(train1 == (char)possibleTrains2.get(num)) {
            num = rand.nextInt(possibleTrains2.size());
        }

        char train2 = possibleTrains2.get(num);

        // generate Transfer 2
        List<String> possibleTransfers2 = transfersByLine.get(train2);
        num = rand.nextInt(possibleTransfers2.size());
        while(transferStation1.equals(possibleTransfers2.get(num))) {
            num = rand.nextInt(possibleTransfers2.size());
        }

        String transferStation2 = possibleTransfers2.get(num);
        System.out.println("Station 2: " + transferStation2);

        // generate Train Line 3
        List<Character> possibleTrains3 = transfersByStation.get(transferStation2);
        num = rand.nextInt(possibleTrains3.size());
        while(train1 == (char)possibleTrains3.get(num) || train2 == (char)possibleTrains3.get(num)) {
            num = rand.nextInt(possibleTrains3.size());
        }

        char train3 = possibleTrains3.get(num);

        // generation start

        return Character.toString(train1) + "->" + transferStation1 + "->" + 
                Character.toString(train2) + "->" + transferStation2 + "->" +
                Character.toString(train3);
    }
}
