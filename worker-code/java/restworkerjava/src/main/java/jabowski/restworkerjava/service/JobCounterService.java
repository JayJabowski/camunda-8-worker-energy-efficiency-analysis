package jabowski.restworkerjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class JobCounterService {

    private final Counter jobsStarted; 
    private final Counter jobsFinished;
    
    @Autowired
    public JobCounterService(MeterRegistry registry){
        this.jobsStarted = Counter.builder("restworkerjava_jobs_started").description("Increases for every job handling started").register(registry);
        this.jobsFinished = Counter.builder("restworkerjava_jobs_finished").description("Increases for every job handling finished").register(registry);
    }

    public void incrementJobsStarted(){
        jobsStarted.increment();
    }

    public void incrementJobsFinished(){
        jobsFinished.increment();
    }


}
