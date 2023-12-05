package jabowski.restworkerjava.entities;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Content of C8-Benchmark file: 

    typical_payload.json

{   
    "var1": "value1", 
    "var2": true, 
    "var3": 15, 
    "var4": { 
            "var4-1": "value4-1-${RANDOM_UUID}", 
            "var4-2": false, 
            "var4-3": 111 
        }, 
    "var5": "736d9100-0155-4af5-be14-b09c42de8417", 
    "var6": "b2959d57-d091-42d4-b18c-9e2145b45074", 
    "var7": "572c74fa-fb3d-4711-bb76-21d66b87fa86 ", 
    "var8": "d091-42d4-b18c-9e2145b45074-b2959d57", 
    "var9": "b18c-9e2145b45074-b2959d57-d091-42d4", 
    "var10": "b2959d5742d4-b18c-d091-9e2145b45074", 
    "var11": "b18c-9e2145b45074-b2959d57-d091-42d4", 
    "var12": 7458, 
    "var13": false, 
    "list": ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j"] 
}

 */

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypicalPayload {

    String var1, var5, var6, var7, var8, var9, var10, var11;
    boolean var2, var13;
    Integer var3, var12;
    Var4 var4;
    ArrayList<String> list;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Var4{
        String var4_1;
        boolean var4_2;
        Integer var4_3;
    }
    
}
