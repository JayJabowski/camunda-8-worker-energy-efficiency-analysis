package jabowski.restworkerjava.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import jabowski.restworkerjava.worker.FetchJSONHandler;

public class updateableWorkerWrapperTest {

    /*
     * 
     @Autowired
     ZeebeClientLifecycle client;
     
     @BeforeEach
     void setup(){
         JobHandler handler = new FetchJSONHandler();
         
         updateableWorkerWrapper worker = new updateableWorkerWrapper();

         worker
         .setJobType("fetch-json")
         .setJobHandler(handler)
         .setClient(client);
        }
        
        @Test
        void testStartWorker() {
            
        }
        
        @Test
        void testUpdateConfig() {
            
        }
        */
    }
