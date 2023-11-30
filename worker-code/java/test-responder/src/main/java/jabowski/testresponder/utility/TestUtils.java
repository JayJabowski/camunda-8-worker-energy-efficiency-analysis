package jabowski.testresponder.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    public Map<Integer, String> getMap(int size){
        Map<Integer, String> map = new HashMap<Integer,String>();

        for (int i=0; i<size; i++){

            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 16;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(j -> (j <= 57 || j >= 65) && (j <= 90 || j >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

            map.put(i, generatedString);
        }

        return map;
    }
    
}
