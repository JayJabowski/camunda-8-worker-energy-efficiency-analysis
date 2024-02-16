package jabowski.restworkerjava.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jabowski.restworkerjava.entities.TypicalPayload;

@Component
public class TypicalPayloadMapper {
    
        public static Map<String, Object> mapPayload(TypicalPayload payload) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = payload.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(payload));
        }
        return map;
    }

}
