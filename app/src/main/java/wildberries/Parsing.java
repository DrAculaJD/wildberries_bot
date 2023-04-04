package wildberries;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.typesOfOperations.Order;
import wildberries.typesOfOperations.Sale;

public class Parsing {

    private static String reportMessage;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static List<?> parsingData(String data, String typeOfData) {
        List<?> result = null;

        try {

            if (typeOfData.equals("order")) {
                result = MAPPER.readValue(data, new TypeReference<List<Order>>() {
                });
                reportMessage = "Заказ №";
            } else if (typeOfData.equals("sale")) {
                result = MAPPER.readValue(data, new TypeReference<List<Sale>>() {
                });
                reportMessage = "Продажа №";
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String dataToString(String data, String typeOfData) {

        final StringBuilder builder = new StringBuilder();
        int counter = 1;

        if (parsingData(data, typeOfData).isEmpty()) {
            return "Данных за сегодня еще нет \uD83D\uDCB9";
        }

        for (Object str : parsingData(data, typeOfData)) {
            builder.append(reportMessage).append(counter).append("\n");
            builder.append(str.toString()).append("\n");
            counter++;
        }

        return builder.substring(0, builder.length() - 2);
    }
}
