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
    private static final String PRE_URL = "https://statistics-api.wildberries.ru/api/v1/supplier";

    public static String getDataForTheDay(String apiKey, TypeOfOperations type) {
        String result;

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getUrl(type))
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

    private static String getUrl(TypeOfOperations type) {
        String request;

        if (type.equals(TypeOfOperations.SALE)) {
            request = SALES_REQUEST;
        } else {
            request = ORDERS_REQUEST;
        }

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(PRE_URL + request)).newBuilder();
        urlBuilder.addQueryParameter("dateFrom", getDate());
        urlBuilder.addQueryParameter("flag", "1");

        return urlBuilder.build().toString();
    }

    private static String getDate() {
        final LocalDate currentDate = LocalDate.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);
    }
}
