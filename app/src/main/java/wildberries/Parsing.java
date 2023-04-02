package wildberries;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.types.Orders;

public class Parsing {

    private static List<Orders> parsing(String data) {
        final ObjectMapper mapper = new ObjectMapper();
        List<Orders> result;

        try {
            result = mapper.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String dataToString(String data) {
        StringBuilder result = new StringBuilder();
        int counter = 1;

        if (parsing(data).isEmpty()) {
            return "Сегодня заказов еще не было.";
        }

        for (Orders str: parsing(data)) {
            result.append("Заказ №").append(counter).append("\n");
            result.append(str.toString()).append("\n");
            counter++;
        }

        return result.substring(0, result.length() - 2);
    }
}
