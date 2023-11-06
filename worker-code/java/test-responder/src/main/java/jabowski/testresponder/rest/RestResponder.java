package jabowski.testresponder.rest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jabowski.testresponder.utility.TestUtils;

@RestController
public class RestResponder {
    //Size of Json Hashmap of random strings
    private final int JSONMapSize = 64;

    //time to wait before responding
    private final int waitTime = 500;

    @Autowired
    TestUtils utils;

    @GetMapping("/testHello")
    public String getTestHello(){
        return "Hello From TestResponder";
    }

    @GetMapping("/testJSON")
    public Map<Integer, String> getTestJson() throws InterruptedException{
        
        TimeUnit.MILLISECONDS.sleep(waitTime);

        return utils.getMap(JSONMapSize);
    }
    
}
