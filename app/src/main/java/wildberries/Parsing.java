package wildberries;

import java.util.ArrayList;
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

    // метод обрабатывает данные после их получения с сервера
    public static String dataToString(String chatId, String data, TypeOfOperations typeOfOperations) {
        // парсинг данных из json в List объектов интерфейса DataFromWildberries
        List<? extends DataFromWildberries> dataToFormat = parsingData(data, typeOfOperations);

        // проверки: являются ли эти данные отзывами или вопросами клиентов
        final boolean isFeedback = typeOfOperations.equals(TypeOfOperations.FEEDBACKS);
        final boolean isQuestion = typeOfOperations.equals(TypeOfOperations.QUESTIONS);

        if (dataToFormat.size() == 0) {
            // если данных нет, пользователю возвращается соответсвующее сообщение
            return "Данных за сегодня еще нет \uD83D\uDCB9";
        } else if (isQuestion || isFeedback) {
            // если данные являются отзывами или вопросами клиентов,
            // происходит их форматирование, определенное в методе toString() объекта обработки
            String result = null;
            for (DataFromWildberries obj: dataToFormat) {
                result = obj.toString(chatId);
            }
            return result.substring(0, result.length() - 2);
        }

        // если данные не являются отзывами или вопросами, тогда они обрабатываются как заказы или продажи
        return ordersOrSalesToString(dataToFormat, typeOfOperations);
    }

    // метод для обработки и форматирования заказов и продаж
    private static String ordersOrSalesToString(List<? extends DataFromWildberries> dataToFormat,
                                                TypeOfOperations typeOfOperations) {
        // инициализируется переменная для хранения форматированных данных
        final StringBuilder builder = new StringBuilder();
        // наименование единицы объекта выбирается в зависимости от типа объекта
        // и записывается в переменную reportMessage
        final String reportMessage = getNameOfOperation(typeOfOperations);
        // для нумерации объектов инициализируется счетчик counter
        int counter = 1;

        // осуществляется проход по объектам и их форматирование
        for (DataFromWildberries str: dataToFormat) {
            builder.append(reportMessage).append(counter).append("\n");
            builder.append(str.toString()).append("\n");
            counter++;
        }

        //возвращается результат форматирования за вычетом двух последних переносов строки
        final int numberOfExtraLineBreaks = 2;
        return builder.substring(0, builder.length() - numberOfExtraLineBreaks);
    }

    // метод для парсинга json в List объектов интерфейса DataFromWildberries
    private static List<? extends DataFromWildberries> parsingData(String data, TypeOfOperations typeOfOperations) {
        final ObjectMapper mapper = new ObjectMapper();
        List<? extends DataFromWildberries> result = new ArrayList<>();

        try {
            // в зависимости от типа аргумента typeOfOperations выбирается класс,
            // согласно которому будут парситься данные
            if (typeOfOperations == TypeOfOperations.ORDER) {
                result = mapper.readValue(data, new TypeReference<List<Order>>() {
                });
            } else if (typeOfOperations == TypeOfOperations.SALE) {
                result = mapper.readValue(data, new TypeReference<List<Sale>>() {
                });
            } else if (typeOfOperations == TypeOfOperations.FEEDBACKS) {
                result = Collections.singletonList(mapper.readValue(data, Feedback.class));
            } else if (typeOfOperations == TypeOfOperations.QUESTIONS) {
                result = Collections.singletonList(mapper.readValue(data, Question.class));
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // метод для выбора наименования объектов для их форматирования в методе ordersOrSalesToString
    private static String getNameOfOperation(TypeOfOperations typeOfOperations) {
        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            return "Заказ №";
        }

        return "Продажа №";
    }

}
