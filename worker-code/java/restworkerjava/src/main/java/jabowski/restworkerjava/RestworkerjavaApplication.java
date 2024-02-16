package jabowski.restworkerjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jabowski.restworkerjava.controller.WorkerController;
import jabowski.restworkerjava.service.JobCounterService;
import jabowski.restworkerjava.worker.FetchJSONHandlerReactive;

@SpringBootApplication
public class RestworkerjavaApplication{

    private final static Logger LOG = LoggerFactory.getLogger(RestworkerjavaApplication.class);
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RestworkerjavaApplication.class, args);
		
		JobCounterService counter = ctx.getBean(JobCounterService.class);
		FetchJSONHandlerReactive handler = ctx.getBean(FetchJSONHandlerReactive.class);
		handler.setCounter(counter);
	
		ctx.getBean(WorkerController.class).initializeWorker("fetch-json", handler);
		LOG.info("Started reactive Worker");


	}

}