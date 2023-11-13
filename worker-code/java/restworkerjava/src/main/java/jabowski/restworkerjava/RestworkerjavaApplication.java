package jabowski.restworkerjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jabowski.restworkerjava.controller.WorkerController;
import jabowski.restworkerjava.worker.FetchJSONHandler;

@SpringBootApplication
public class RestworkerjavaApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RestworkerjavaApplication.class, args);

		ctx.getBean(WorkerController.class).initializeWorker("fetch-json", new FetchJSONHandler());
	}

}
	