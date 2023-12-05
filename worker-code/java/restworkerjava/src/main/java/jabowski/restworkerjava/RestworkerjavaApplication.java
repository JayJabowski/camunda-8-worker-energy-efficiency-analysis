package jabowski.restworkerjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jabowski.restworkerjava.controller.WorkerController;
import jabowski.restworkerjava.rest.RestCallApi;
import jabowski.restworkerjava.worker.FetchJSONHandlerReactive;

@SpringBootApplication
public class RestworkerjavaApplication{
	@Value("${blocking.worker.active:true}")
	static Boolean blockingActive;
	@Value("${reactive.worker.active:true}")
	static Boolean reactiveActive;

    private final static Logger LOG = LoggerFactory.getLogger(RestworkerjavaApplication.class);
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RestworkerjavaApplication.class, args);
		
		LOG.info(reactiveActive + " " + blockingActive);
		
		RestCallApi api = ctx.getBean(RestCallApi.class);
	
		/*
		 * 
		 if(blockingActive == true) 
		 {
			 LOG.info("Started blocking Worker");
			 ctx.getBean(WorkerController.class).initializeWorker("fetch-json", new FetchJSONHandler(api));
		 }
		*/
		if(/* reactiveActive == */true) 
		{
			LOG.info("Started reactive Worker");
			ctx.getBean(WorkerController.class).initializeWorker("fetch-json", new FetchJSONHandlerReactive(api));
		}
	}

}