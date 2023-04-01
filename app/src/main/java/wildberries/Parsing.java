package wildberries;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.types.Orders;

public class Parsing {

    public static List<Orders> parsing(String data) {
        final ObjectMapper mapper = new ObjectMapper();
        List<Orders> result;

        try {
            result = mapper.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(dataToString(result));
        return result;
    }

    public static String dataToString(List<Orders> orders) {
        StringBuilder result = new StringBuilder();
        int counter = 1;

        for (Orders str: orders) {
            result.append("Заказ №").append(counter).append("\n");
            result.append(str.toString()).append("\n");
            counter++;
        }

        return result.toString();
    }
}
