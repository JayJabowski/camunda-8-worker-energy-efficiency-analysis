package jabowski.restworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.config.ZeebeClientStarterAutoConfiguration;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;

@SpringBootApplication
@EnableZeebeClient
@Import(ZeebeClientStarterAutoConfiguration.class)
public class RestWorkerApplication implements CommandLineRunner {

	private final static Logger LOG = LoggerFactory.getLogger(RestWorkerApplication.class);

	public static void main(String[] args) {
	
		SpringApplication.run(RestWorkerApplication.class, args);
	}

	@Autowired
	private ZeebeClientLifecycle client;



	@Override
	public void run(String... args) throws Exception {

		
		//Uncomment to start an Instance at each deploy
		/* 
		 final ProcessInstanceEvent event = 
		 client
		 .newCreateInstanceCommand()
		 .bpmnProcessId("Single_Worker_Timed")
		 .latestVersion()
		 .send()
		 .join();
		 
		 LOG.info("Started using "+event.getBpmnProcessId()+", "+event.getProcessInstanceKey());
		*/
			
	}


}
