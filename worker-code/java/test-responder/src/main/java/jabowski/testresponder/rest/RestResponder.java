package jabowski.testresponder.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jabowski.testresponder.entities.TypicalPayload;
import jabowski.testresponder.utility.TestUtils;

@RestController
public class RestResponder {
    //Size of Json Hashmap of random strings
    private final int JSONMapSize = 64;

    //time to wait before responding
    //TODO: refactor as env
    private final int waitTime = 500;

    @Autowired
    TestUtils utils;

    @GetMapping("/testHello")
    public String getTestHello(){
        return "Hello From TestResponder";
    }

    @GetMapping("/testJSON")
    public ResponseEntity<TypicalPayload> getTestJson() throws InterruptedException, IOException{
        TimeUnit.MILLISECONDS.sleep(waitTime);

        return ResponseEntity.ok(new TypicalPayload());
    }
    
}
