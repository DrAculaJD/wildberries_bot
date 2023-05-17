package wildberries;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import wildberries.typesOfOperations.standart.Feedback;
import wildberries.typesOfOperations.standart.Question;
import wildberries.typesOfOperations.statistics.Order;
import wildberries.typesOfOperations.statistics.Sale;
import wildberries.typesOfOperations.DataFromWildberries;
import wildberries.typesOfOperations.TypeOfOperations;

public class Parsing {

    public static String dataToString(String data, TypeOfOperations typeOfOperations) {
        List<? extends DataFromWildberries> dataToFormat = parsingData(data, typeOfOperations);
        final boolean isFeedback = typeOfOperations.equals(TypeOfOperations.FEEDBACKS);
        final boolean isQuestion = typeOfOperations.equals(TypeOfOperations.QUESTIONS);

        if (dataToFormat.size() == 0) {
            return "Данных за сегодня еще нет \uD83D\uDCB9";
        } else if (isQuestion || isFeedback) {
            final String result = dataToFormat.toString();
            return result.substring(1, result.length() - 2);
        }

        return ordersOrSalesToString(dataToFormat, typeOfOperations);
    }

    private static String ordersOrSalesToString(List<? extends DataFromWildberries> dataToFormat,
                                                TypeOfOperations typeOfOperations) {
        final StringBuilder builder = new StringBuilder();
        final String reportMessage = getNameOfOperation(typeOfOperations);
        int counter = 1;

        for (DataFromWildberries str: dataToFormat) {
            builder.append(reportMessage).append(counter).append("\n");
            builder.append(str.toString()).append("\n");
            counter++;
        }

        return builder.substring(0, builder.length() - 2);
    }

    private static List<? extends DataFromWildberries> parsingData(String data, TypeOfOperations typeOfOperations) {
        final ObjectMapper mapper = new ObjectMapper();
        List<? extends DataFromWildberries> result = null;

        try {

            if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
                result = mapper.readValue(data, new TypeReference<List<Order>>() {
                });
            } else if (typeOfOperations.equals(TypeOfOperations.SALE)) {
                result = mapper.readValue(data, new TypeReference<List<Sale>>() {
                });
            } else if (typeOfOperations.equals(TypeOfOperations.FEEDBACKS)) {
                result = Collections.singletonList(mapper.readValue(data, Feedback.class));
            } else if (typeOfOperations.equals(TypeOfOperations.QUESTIONS)) {
                result = Collections.singletonList(mapper.readValue(data, Question.class));
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static String getNameOfOperation(TypeOfOperations typeOfOperations) {
        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            return "Заказ №";
        }

        return "Продажа №";
    }

}
