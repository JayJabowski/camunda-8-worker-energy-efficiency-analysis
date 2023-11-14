package jabowski.restworkerjava.service;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.api.worker.JobHandler;

@Service
public class HandlerFactoryService {
    
    public static <T extends JobHandler> T createHandler(String handlerName) 
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, SecurityException{
                
                Class<?> clazz = Class.forName(handlerName);

                @SuppressWarnings("unchecked")
                T instance = (T) clazz.getDeclaredConstructor().newInstance();

                return instance;
            }
}
