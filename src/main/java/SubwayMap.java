package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubwayMap {
    // String : train, List<String> : stations
    // Stores a list of stations for each line
    private HashMap<Character, List<String>> linesByStation;

    // String : train line, List<String> : stations
    // Stores a list of transfer stations for each line
    private HashMap<Character, List<String>> transfersByLine;

    // String : station name, List<Character> : list of lines available --> duplicate stations will be listed separately (deal with connections)
    // Stores a list of lines available at each transfer station
    private HashMap<String, List<Character>> transfersByStation;

    char[] trains = new char[]{'1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'};

    public SubwayMap() {
        linesByStation = new HashMap<>();
        transfersByLine = new HashMap<>();
        transfersByStation = new HashMap<>();

        sortByLines();
        sortByTransfers();
        sortTransfers();
    }

    private void sortByLines() {
        File file;
        BufferedReader br;
        try {
            for(char train : trains) {
                String filePath = "src/Stations/" + Character.toString(train) + "_train.txt";
                file = new File(filePath);

                br = new BufferedReader(new FileReader(file));
                br.readLine();

                List<String> stations = new ArrayList<>();

                String line = br.readLine();
                while(line != null) {
                    stations.add(line.strip());
                    line = br.readLine();
                }

                linesByStation.put(train, stations);
                br.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("TRAIN FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    private void sortByTransfers() {
        File file = new File("src/Transfers/transfers_copy.txt");
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
                        stations.add(stationName.strip());
                    } else {
                        List<String> stations = new ArrayList<>();
                        stations.add(stationName.strip());
                        transfersByLine.put(train, stations);
                    }
                }

                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("TRANSFERS FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    // TODO : COMBINE THIS WITH SORTBYTRANSFERS()
    private void sortTransfers() {
        try {
            File file = new File("src/Transfers/transfers_copy.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));  

            String line = br.readLine();
            while(line != null) {
                int index = line.indexOf(" [");
                String stationName = line.substring(0,index).strip();

                List<Character> trainsAtStation = new ArrayList<>();
                for(char ch : line.substring(index).toCharArray()) {
                    if(ch != ' ' && ch != ',' && ch != '[' && ch != ']') {
                        trainsAtStation.add(ch);
                    }
                }

                transfersByStation.put(stationName.strip(), trainsAtStation);

                line = br.readLine();
            }

            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("TRANSFERS FILE NOT FOUND");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    public HashMap<Character, List<String>> getLinesByStation() {
        return linesByStation;
    }

    public List<String> getStations(char train) {
        return linesByStation.get(train);
    }

    public HashMap<Character, List<String>> getTransfersByLine() {
        return transfersByLine;
    }

    public List<String> getTransfers(char train) {
        return transfersByLine.get(train);
    }

    public HashMap<String, List<Character>> getTransfersByStation() {
        return transfersByStation;
    }

    public List<Character> getTrains(String station) {
        return transfersByStation.get(station);
    }
}
