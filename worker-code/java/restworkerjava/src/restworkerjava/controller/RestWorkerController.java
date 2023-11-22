package jabowski.restworkerjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.eventbus.EventBus;

import jabowski.restworkerjava.events.ConfigUpdateEvent;
import jabowski.restworkerjava.worker.updateableWorkerWrapper;

@RestController
public class RestWorkerController {
    /*
     * 
     @Autowired
     EventBus eventbus;
     
     @Autowired
     updateableWorkerWrapper worker;

    @PostMapping("/config")
    public ConfigUpdateEvent postNewConfig(@RequestBody ConfigUpdateEvent e){
        
        eventbus.post(e);
        
        worker.updateConfig(e);
        
        return e;
    */  
    }


