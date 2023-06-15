package telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;
import wildberries.typesOfOperations.standart.answers.FeedbacksAnswer;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.Main.userSQL;
import static wildberries.WBdata.getPreUrl;
import static wildberries.WBdata.getRequest;

public class Answers {

    // список для хранения чатов, от которых ожидается ответ на отзыв/вопрос
    public static Map<String, TypeOfOperations> answerers = new HashMap<>();
    private static final String NAME_OF_FEEDBACKS = "отзывов";
    private static final String NAME_OF_QUESTIONS = "вопросов";

    // метод для вывода сообщения, в котором пользователю предлагается ввести ответ на отзыв или вопрос
    public static SendMessage enterAnswerMessage(String chatId, TypeOfOperations typeOfOperations,
                                                 TypeOfApi typeOfApi) {
        final SendMessage outputMessage = new SendMessage();
        // получение необходимого api ключа для получения списка вопросов/отзывов
        final String api = userSQL.getApi(chatId, typeOfApi);

        // получение и форматирование списка вопросов/отзывов
        final String dataForTheDay = WBdata.getDataForTheDay(api, typeOfOperations, typeOfApi);
        final String formatData = Parsing.dataToString(chatId, dataForTheDay, typeOfOperations);
        // инициализация переменной, которая будет обозначать вопрос/отзыв в сообщении
        final String objectName = getNameOfObject(typeOfOperations);
        // инициализация сообщения для случая, если нет отзывов/вопросов без ответа
        final String noDataMessage = objectName.substring(0, 1).toUpperCase()
                + objectName.substring(1) + " без ответа еще нет ⛔";

        if (formatData.equals(noDataMessage)) {
            // вывод сообщения в случае, если нет отзывов/вопросов без ответа
            outputMessage.setText(noDataMessage);
        } else {
            // сообщение с предложением пользователю ввести ответ на первый вопрос
            final String firstMessage = "Вот список " + objectName + " без ответа:\n";
            final String lastMessage = "\nВаше следующее сообщение будет отправлено как ответ " +
                    "на первый из " + objectName + " \uD83D\uDC47";
            outputMessage.setText(firstMessage + formatData + lastMessage);

            // id чата пользователя добавляется в список answerers
            answerers.put(chatId, TypeOfOperations.FEEDBACKS);
        }

        outputMessage.setChatId(chatId);

        return outputMessage;
    }

    public static SendMessage sendAnswer(String chatId, String answersMessage) throws JsonProcessingException {
        final String apiKey = userSQL.getApi(chatId, TypeOfApi.STANDART_API);
        final SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        final TypeOfOperations typeOfOperations = answerers.get(chatId);

        // тело запроса в формате JSON
        final String jsonBody = getJsonBody(chatId, typeOfOperations, answersMessage);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getUrl(answerers.get(chatId)))
                .addHeader("Authorization", "Bearer " + apiKey)
                .patch(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        //System.out.println(request);

        try (Response response = client.newCall(request).execute()) {
            outputMessage.setText("Ответ успешно отправлен! \uD83D\uDE4C");
            answerers.remove(chatId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return outputMessage;
    }

    private static String getJsonBody(String chatId, TypeOfOperations typeOfOperations, String answersMessage)
            throws JsonProcessingException {
        final String objectId = OneObject.getFirstObjectId(chatId,typeOfOperations);

        if (typeOfOperations.equals(TypeOfOperations.FEEDBACKS)) {
            final FeedbacksAnswer answer = new FeedbacksAnswer(objectId, answersMessage);

            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(answer);
        }

        return null; // дописать логику для ответа на вопрос!!!
    }

    private static String getUrl(TypeOfOperations typeOfOperations) {
        String request = getRequest(typeOfOperations, TypeOfApi.STANDART_API);

        HttpUrl.Builder urlBuilder =
                Objects.requireNonNull(HttpUrl.parse(getPreUrl(TypeOfApi.STANDART_API)
                        + request)).newBuilder();

        return urlBuilder.build().toString();
    }

    // метод для выбора переменной, которая будет обозначать вопрос/отзыв в сообщении
    private static String getNameOfObject(TypeOfOperations typeOfOperations) {
        if (typeOfOperations.equals(TypeOfOperations.QUESTIONS)) {
            return NAME_OF_QUESTIONS;
        }

        return NAME_OF_FEEDBACKS;
    }
}
