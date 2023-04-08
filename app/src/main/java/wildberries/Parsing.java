package wildberries;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.typesOfOperations.Order;
import wildberries.typesOfOperations.Sale;
import wildberries.typesOfOperations.StatisticsData;

public class Parsing {

    private static String reportMessage;

    public static String dataToString(String data, String typeOfData) {
        List<? extends StatisticsData> filteredData = dataFiltering(data, typeOfData);
        final StringBuilder builder = new StringBuilder();
        int counter = 1;

        if (filteredData.size() == 0) {
            return "Данных за сегодня еще нет \uD83D\uDCB9";
        }

        for (Object str : filteredData) {
            builder.append(reportMessage).append(counter).append("\n");
            builder.append(str.toString()).append("\n");
            counter++;
        }

        return builder.substring(0, builder.length() - 2);
    }

    private static List<? extends StatisticsData> parsingData(String data, String typeOfData) {
        final ObjectMapper mapper = new ObjectMapper();
        List<? extends StatisticsData> result = null;

        try {

            if (typeOfData.equals("order")) {
                result = mapper.readValue(data, new TypeReference<List<Order>>() {
                });
                reportMessage = "Заказ №";
            } else if (typeOfData.equals("sale")) {
                result = mapper.readValue(data, new TypeReference<List<Sale>>() {
                });
                reportMessage = "Продажа №";
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static List<? extends StatisticsData> dataFiltering(String data, String typeOfData) {
        final String todayDate = WBdata.getDate();
        List<? extends StatisticsData> dataToFilter = parsingData(data, typeOfData);

        return dataToFilter.stream()
                .filter(dat -> dat.getLastChangeOrderDate().equals(dat.getDate()) && dat.getDate().equals(todayDate))
                .toList();
    }
}
