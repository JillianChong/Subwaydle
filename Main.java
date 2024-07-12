import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    // String : train line, List<String> : stations
    public static HashMap<Character, List<String>> transfersByLine = new HashMap<>();
    // String : 
    public static HashMap<Character, List<String>> linesByStation = new HashMap<>();

    public static void main(String[] args) {
        sortByTransfers();
        // printHashMap();
        System.out.println(printList(transfersByLine.get('Z')));
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
    public static void printHashMap() { 
        for(Character train : transfersByLine.keySet()) {
            System.out.println(Character.toString(train) + " Train: " + printList(transfersByLine.get(train)));
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

}
