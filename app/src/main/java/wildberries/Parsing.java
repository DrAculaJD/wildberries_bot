package wildberries;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.typesOfOperations.Order;
import wildberries.typesOfOperations.Sale;

public class Parsing {

    private static List<Order> parsingOrders(String data) {
        final ObjectMapper mapper = new ObjectMapper();
        List<Order> result;

        try {
            result = mapper.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static List<Sale> parsingSales(String data) {
        final ObjectMapper mapper = new ObjectMapper();
        List<Sale> result;

        try {
            result = mapper.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String ordersToString(String data) {
        StringBuilder result = new StringBuilder();
        int counter = 1;

        if (parsingOrders(data).isEmpty()) {
            return "Сегодня заказов еще не было \uD83D\uDCB9";
        }

        for (Order str: parsingOrders(data)) {
            result.append("Заказ №").append(counter).append("\n");
            result.append(str.toString()).append("\n");
            counter++;
        }

        return result.substring(0, result.length() - 2);
    }

    public static String salesToString(String data) {
        StringBuilder result = new StringBuilder();
        int counter = 1;

        if (parsingOrders(data).isEmpty()) {
            return "Сегодня продаж еще не было \uD83D\uDCB9";
        }

        for (Order str: parsingOrders(data)) {
            result.append("Продажа №").append(counter).append("\n");
            result.append(str.toString()).append("\n");
            counter++;
        }

        return result.substring(0, result.length() - 2);
    }
}
