package wildberries;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WBdata {

    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static  String formattedDate = CURRENT_DATE.format(FORMATTER);

    public static void setFormattedDate(String formattedDate) {
        WBdata.formattedDate = formattedDate;
    }

    public static String getOrdersForTheDay(String apiKey) {
        String url = "https://statistics-api.wildberries.ru/api/v1/supplier/orders?dateFrom=" + formattedDate;

        String result;

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            Response response = client.newCall(request).execute();

            result = response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String getSalesForTheDay(String apiKey) {
        String url = "https://statistics-api.wildberries.ru/api/v1/supplier/sales?dateFrom=" + formattedDate;

        String result;

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
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
