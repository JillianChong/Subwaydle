package com.example.subwaydle;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    String greenHex = "#27BB5F";
    String blueHex = "#27A3BB";
    String yellowHex = "#F0DF4F";
    // String grayHex = "#C7C7C4";
    String grayHex = "#959ba1";

    int[] guessResults;

    @GetMapping("/generatePath")
    public String[] generatePath() {
        String[] pathInfo = SubwaydleApplication.generatePathToController();

        return pathInfo;
    }

    @GetMapping("/printPath")
    public String printPath() {
        return SubwaydleApplication.sendPathToController();
    }

    @PostMapping("/submitData")
    public String receiveData(@RequestBody String[] received_data) {
        // DO SOMETHING; -- pass data to subwaydle application to get back results of each box
        char[] guess = new char[3];
        for(int i = 0; i < guess.length; i++) {
            guess[i] = received_data[i].charAt(0);
        }

        guessResults = SubwaydleApplication.sendAnswerToController(guess);

        return "Data received successfully";
    }

    // Updates colors of row
    @GetMapping("/updateBoard")
    public String[] updateBoard(Model model) {
        String[] colors = new String[3];
        for(int i = 0; i < guessResults.length; i++) {
            int result = guessResults[i];

            if(result == 0) {
                colors[i] = greenHex;
            } else if(result == 1) {
                colors[i] = blueHex;
            } else if(result == 2) {
                colors[i] = yellowHex;
            } else {
                colors[i] = grayHex;
            }
        }

        return colors;
    }

    @PostMapping("/getDisplayName")
    public String getDisplayName(@RequestBody List<String> input) {
        String codeName = input.get(0);
        char train = input.get(1).charAt(0);
        
        return SubwaydleApplication.sendDisplayNameToController(codeName, train);
    }
}
