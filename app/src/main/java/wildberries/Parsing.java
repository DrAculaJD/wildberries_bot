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

/**
 * Класс содержит методы для парсинга и форматирования данных, которые поступают от Wildberries.
 */
public class Parsing {

    /**
     * Метод форматирует данные после их получения с сервера Wildberries.
     * @param chatId ID Telegram чата пользователя
     * @param data данные для форматирования
     * @param typeOfOperations вид данных, с которыми работает метод
     * @return отформатированные данные
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
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

    /**
     * Метод для обработки и форматирования только заказов и продаж.
     * @param dataToFormat список данных для форматирования
     * @param typeOfOperations вид данных, с которыми работает метод
     * @return отформатированные данные
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    private static String ordersOrSalesToString(List<? extends DataFromWildberries> dataToFormat,
                                                TypeOfOperations typeOfOperations) {
        // инициализируется переменная для хранения форматированных данных
        final StringBuilder builder = new StringBuilder();
        // наименование единицы объекта выбирается в зависимости от вида данных
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

    /**
     * Метод для парсинга данных из формата JSON в List объектов интерфейса <b>DataFromWildberries</b>.
     * @param data данные для парсинга
     * @param typeOfOperations вид данных, с которыми работает метод
     * @return список объектов интерфейса <b>DataFromWildberries</b>
     * @see wildberries.typesOfOperations.TypeOfOperations
     * @see wildberries.typesOfOperations.DataFromWildberries
     */
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

    /**
     * Метод выбирает наименование данных для их форматирования в методе <b>ordersOrSalesToString</b>.
     * @param typeOfOperations вид данных, с которыми работает метод
     * @return наименование данных для их форматирования в методе <b>ordersOrSalesToString</b>.
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    private static String getNameOfOperation(TypeOfOperations typeOfOperations) {
        if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
            return "Заказ №";
        }

        return "Продажа №";
    }

}
