package wildberries;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WBdata {

    private static final String SALES_REQUEST = "/sales?dateFrom=";
    private static final String ORDERS_REQUEST = "/orders?dateFrom=";
    private static  String currentRequest;
    private static final String PRE_URL = "https://statistics-api.wildberries.ru/api/v1/supplier";

    public static void setCurrentRequest(String typeOfData) {

        if (typeOfData.equals("sale")) {
            currentRequest = SALES_REQUEST;
        } else if (typeOfData.equals("order")) {
            currentRequest = ORDERS_REQUEST;
        }

    }

    public static String getDate() {
        final ZoneId zoneId = ZoneId.of("Europe/Moscow");
        final LocalDate currentDate = LocalDate.now(zoneId);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);
    }

    public static String getDataForTheDay(String apiKey, String typeOfData) {
        setCurrentRequest(typeOfData);
        final String url = PRE_URL + currentRequest + getDate();

        String result;

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            Response response = client.newCall(request).execute();

            result = response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
