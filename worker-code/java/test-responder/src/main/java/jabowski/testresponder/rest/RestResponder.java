package jabowski.testresponder.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jabowski.testresponder.config.ResponderConfig;
import jabowski.testresponder.entities.TypicalPayload;
import jabowski.testresponder.utility.TestUtils;

@RestController
public class RestResponder {
    //Size of Json Hashmap of random strings
    private final int JSONMapSize = 64;

    @Autowired
    TestUtils utils;

    @Autowired
    ResponderConfig config;

    @GetMapping("/testHello")
    public String getTestHello(){
        return "Hello From TestResponder";
    }

    @GetMapping("/testJSON")
    public ResponseEntity<TypicalPayload> getTestJson() throws InterruptedException, IOException{
        TimeUnit.MILLISECONDS.sleep(config.getWaitTime());

        return ResponseEntity.ok(new TypicalPayload());
    }
    
}
