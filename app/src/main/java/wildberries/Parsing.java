package wildberries;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.typesOfOperations.Order;
import wildberries.typesOfOperations.Sale;

public class Parsing {

    private static final StringBuilder BUILDER = new StringBuilder();
    private static int counter = 1;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static List<Order> parsingOrders(String data) {
        List<Order> result;

        try {
            result = MAPPER.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static List<Sale> parsingSales(String data) {
        List<Sale> result;

        try {
            result = MAPPER.readValue(data, new TypeReference<>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String ordersToString(String data) {

        if (parsingOrders(data).isEmpty()) {
            return "Сегодня заказов еще не было \uD83D\uDCB9";
        }

        for (Order str: parsingOrders(data)) {
            BUILDER.append("Заказ №").append(counter).append("\n");
            BUILDER.append(str.toString()).append("\n");
            counter++;
        }

        return BUILDER.substring(0, BUILDER.length() - 2);
    }

    public static String salesToString(String data) {

        if (parsingSales(data).isEmpty()) {
            return "Сегодня продаж еще не было \uD83D\uDCB9";
        }

        for (Sale str: parsingSales(data)) {
            BUILDER.append("Продажа №").append(counter).append("\n");
            BUILDER.append(str.toString()).append("\n");
            counter++;
        }

        return BUILDER.substring(0, BUILDER.length() - 2);
    }
}
