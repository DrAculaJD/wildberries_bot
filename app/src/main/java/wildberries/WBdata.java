package wildberries;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wildberries.typeOfOperations.TypeOfOperations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Содержит методы для получения данных с сервера Wildberries.
 */
public class WBdata {

    /** Вторая часть URL для запроса продаж.  */
    private static final String SALES_REQUEST = "/sales";
    /** Вторая часть URL для запроса заказов.  */
    private static final String ORDERS_REQUEST = "/orders";
    /** Вторая часть URL для запроса отзывов.  */
    private static final String FEEDBACKS_REQUEST = "/feedbacks";
    /** Вторая часть URL для запроса вопросов.  */
    private static final String QUESTIONS_REQUEST = "/questions";
    /** Первая часть URL для запроса продаж/заказов.  */
    private static final String PRE_URL_STATISTICS = "https://statistics-api.wildberries.ru/api/v1/supplier";
    /** Первая часть URL для запроса отзывов/вопросов.  */
    private static final String PRE_URL_STANDART = "https://feedbacks-api.wb.ru/api/v1";

    /**
     * Получает данные с сервера Wildberries в формате JSON. Какие данные будут получены
     * зависит от переданных параметров.
     * @param apiKey API ключ пользователя
     * @param typeOfOperations тип объекта, с которым работает метод
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @param perMonths Параметр который определяет является ли это запрос получением данных за длительный период
     * @return данные в формате JSON
     * @see wildberries.TypeOfApi
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    public static String getData(String apiKey,
                                 TypeOfOperations typeOfOperations,
                                 TypeOfApi typeOfApi,
                                 boolean perMonths) {
        String result;

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrl(typeOfOperations, typeOfApi, perMonths))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            //System.out.println(request);
            Response response = client.newCall(request).execute();

            result = response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Формирует URL для запроса на сервер Wildberries. Параметры запроса зависят от переданных в метод аргументов.
     * @param typeOfOperations тип объекта, с которым работает метод
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @param perMonths Параметр который определяет является ли это запрос получением данных за длительный период
     * @return URL запроса на сервер Wildberries
     * @see wildberries.TypeOfApi
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    private static String getUrl(TypeOfOperations typeOfOperations, TypeOfApi typeOfApi, boolean perMonths) {
        String request = getRequest(typeOfOperations, typeOfApi);

        HttpUrl.Builder urlBuilder =
                Objects.requireNonNull(HttpUrl.parse(getHalfUrl(typeOfApi) + request)).newBuilder();

        if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            urlBuilder.addQueryParameter("isAnswered", "false");
            urlBuilder.addQueryParameter("take", "20");
            urlBuilder.addQueryParameter("skip", "0");
            return urlBuilder.build().toString();
        }

        urlBuilder.addQueryParameter("dateFrom", getDate(perMonths));
        if (perMonths) {
            urlBuilder.addQueryParameter("flag", "0");
        } else {
            urlBuilder.addQueryParameter("flag", "1");
        }

        return urlBuilder.build().toString();
    }

    /**
     * Выбирает первую чать URL запроса на сервер Wildberries, котроая зависит от того,
     * какие данные будут запрашиваться.
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return PRE_URL_STATISTICS или PRE_URL_STANDART
     * @see wildberries.TypeOfApi
     * @see WBdata#PRE_URL_STATISTICS
     * @see WBdata#PRE_URL_STANDART
     */
    public static String getHalfUrl(TypeOfApi typeOfApi) {
        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            return PRE_URL_STATISTICS;
        }

        return PRE_URL_STANDART;
    }

    /**
     * Возвращает втору часть URL для запроса на сервер Wildberries, котроая зависит от того,
     * какие данные будут запрашиваться.
     * @param typeOfOperations тип объекта, с которым работает метод
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return Одну из следующих переменных:<br>
     * SALES_REQUEST<br>
     * ORDERS_REQUEST<br>
     * QUESTIONS_REQUEST<br>
     * FEEDBACKS_REQUEST
     * @see wildberries.TypeOfApi
     * @see wildberries.typeOfOperations.TypeOfOperations
     * @see WBdata#SALES_REQUEST
     * @see WBdata#ORDERS_REQUEST
     * @see WBdata#QUESTIONS_REQUEST
     * @see WBdata#FEEDBACKS_REQUEST
     */
    public static String getRequest(TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {

        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            if (typeOfOperations.equals(TypeOfOperations.SALE)) {
                return SALES_REQUEST;
            } else if (typeOfOperations.equals(TypeOfOperations.ORDER)) {
                return ORDERS_REQUEST;
            }
        } else if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            if (typeOfOperations.equals(TypeOfOperations.QUESTIONS)) {
                return QUESTIONS_REQUEST;
            }
        }

        return FEEDBACKS_REQUEST;
    }

    /**
     * Метод для получения даты по Московскому часовому поясу в формате "yyyy-MM-dd",
     * который требутся для запроса сгласно API Wildberries. Так как сервер может находиться
     * не в Московском часовом поясе необходимо его указать.
     * @param perMonth Параметр который определяет является ли это запрос получением данных за длительный период.
     * @return Если необходимо получить данные с начала года, тогда метод вернет "2023-01-01".<br>
     * В ином случае метод вернет текущую дату по Московскому часовому поясу в формате "yyyy-MM-dd"
     */
    private static String getDate(boolean perMonth) {

        if (perMonth) {
            return "2023-01-01";
        }
        final ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        final ZonedDateTime currentDate = ZonedDateTime.now(moscowZone);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);
    }
}
