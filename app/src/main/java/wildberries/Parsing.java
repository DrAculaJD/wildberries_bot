package wildberries;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parsing {

    private static Map<Object, Object> resultMap = new HashMap<>();
    private static String data;

    private static Map<Object, Object> parsing() {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            resultMap = mapper.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            System.out.println("Что-то пошло не так...");
        }

        return resultMap;
    }

    public static String dataToString(String dataForParsing) {
        data = dataForParsing;
        parsing();

        StringBuilder result = new StringBuilder();

        for (Map.Entry str: resultMap.entrySet()) {
            result.append(str.toString()).append("\n");
        }

        System.out.println(result);
        return result.toString();
    }
}
