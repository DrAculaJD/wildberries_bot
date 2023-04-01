package wildberries;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WBdata {

    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FORMATTED_DATE = CURRENT_DATE.format(FORMATTER);

    public static String getOrdersForTheDay(String apiKey) {
        String url = "https://statistics-api.wildberries.ru/api/v1/supplier/orders?dateFrom=" + FORMATTED_DATE;

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
            return "К сожалению, этот ключ не работает, проверьте, правильно ли скопирован ключ "
                    + "и попробуйте еще раз \uD83D\uDE80";
        }

        //System.out.println(result);
        return result;
    }
}
