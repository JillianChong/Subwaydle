package com.example.subwaydle;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping("/submitData")
    public String receiveData(@RequestBody String[] received_data) {
        // Handle the data received
        if (received_data != null) {
            System.out.println("Received data: " + String.join(", ", received_data));
        } else {
            System.out.println("Received data is null or empty");
        }
        
        // DO SOMETHING;

        return "Data received successfully";
    }
}
