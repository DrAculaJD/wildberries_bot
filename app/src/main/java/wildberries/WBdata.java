package wildberries;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wildberries.typesOfOperations.TypeOfOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class WBdata {

    private static final String SALES_REQUEST = "/sales";
    private static final String ORDERS_REQUEST = "/orders";
    private static final String FEEDBACKS_REQUEST = "/feedbacks";
    private static final String QUESTIONS_REQUEST = "/questions";
    private static final String PRE_URL_STATISTICS = "https://statistics-api.wildberries.ru/api/v1/supplier";
    private static final String PRE_URL_STANDART = "https://feedbacks-api.wb.ru/api/v1";

    public static String getDataForTheDay(String apiKey, TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {
        String result;

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrl(typeOfOperations, typeOfApi))
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

    private static String getUrl(TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {
        String request = setRequest(typeOfOperations, typeOfApi);


        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(getPreUrl(typeOfApi) + request)).newBuilder();

        if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            urlBuilder.addQueryParameter("isAnswered", "false");
            urlBuilder.addQueryParameter("take", "20");
            urlBuilder.addQueryParameter("skip", "0");
            return urlBuilder.build().toString();
        }

        urlBuilder.addQueryParameter("dateFrom", getDate());
        urlBuilder.addQueryParameter("flag", "1");

        return urlBuilder.build().toString();
    }

    private static String getDate() {
        final LocalDate currentDate = LocalDate.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);
    }

    private static String setRequest(TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {

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

    private static String getPreUrl(TypeOfApi typeOfApi) {
        if (typeOfApi.equals(TypeOfApi.STATISTICS_API)) {
            return PRE_URL_STATISTICS;
        }

        return PRE_URL_STANDART;
    }
}
