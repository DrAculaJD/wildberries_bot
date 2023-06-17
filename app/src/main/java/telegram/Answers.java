package telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;
import wildberries.typesOfOperations.standart.answers.Answer;
import wildberries.typesOfOperations.standart.answers.FeedbacksAnswer;
import wildberries.typesOfOperations.standart.answers.QuestionsAnswer;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.Main.userSQL;
import static wildberries.WBdata.getHalfUrl;
import static wildberries.WBdata.getRequest;

/**
 * Класс содержит методы и данные, которые необходимы для реализации функции ответа на отзывы и вопросы пользователей.
 */

public class Answers {

    /** Список для хранения ID чатов, от которых ожидается ответ на отзыв/вопрос. */
    public static Map<String, TypeOfOperations> answerers = new HashMap<>();
    private static final String NAME_OF_FEEDBACKS = "отзывов";
    private static final String NAME_OF_QUESTIONS = "вопросов";

    /**
     * Метод для формирования сообщения, в котором пользователю предлагается ввести ответ на отзыв или вопрос.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return объект <b>SendMessage</b>, который содержит сообщение для пользователя
     * @see wildberries.TypeOfApi
     * @see wildberries.typesOfOperations.TypeOfOperations
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage getFirstMessage(String chatId, TypeOfOperations typeOfOperations,
                                              TypeOfApi typeOfApi) {
        final SendMessage outputMessage = new SendMessage();
        // получение api ключа, необходимого для получения списка вопросов/отзывов
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
            // сообщение с предложением пользователю ввести ответ на первый вопрос/отзыв
            final String firstMessage = "Вот список " + objectName + " без ответа:\n\n";
            final String lastMessage = "\n\nВаше следующее сообщение будет отправлено как ответ "
                    + "на первый из " + objectName + " \uD83D\uDC47";
            outputMessage.setText(firstMessage + formatData + lastMessage);

            // id чата пользователя добавляется в список answerers, чтобы определить
            // следующее сообщение от этого пользователя как ответ на вопрос/отзыв
            answerers.put(chatId, typeOfOperations);
        }

        outputMessage.setChatId(chatId);
        return outputMessage;
    }

    /**
     * Метод для парсинга ответа на отзыв/вопрос в JSON и его отправки на Wildberries.
     * @param chatId ID Telegram чата пользователя
     * @param answersMessage сообщение пользователя, которое он ввел в качестве ответа
     * @return Возвращает объект <b>SendMessage</b>, в котором содержится сообщение об успешной или неуспешной
     * отправке ответа на отзыв/вопрос.
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage sendAnswer(String chatId, String answersMessage) throws JsonProcessingException {
        final String apiKey = userSQL.getApi(chatId, TypeOfApi.STANDART_API);
        final SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        // на что именно будет формиироваться ответ зависит от переменной типа TypeOfOperations,
        // которая получается из списка answerers
        final TypeOfOperations typeOfOperations = answerers.get(chatId);
        //System.out.println("typeOfOperations = " + typeOfOperations);

        // получение тела запроса в формате JSON
        final String jsonBody = getJsonBody(chatId, typeOfOperations, answersMessage);

        // формирование запроса согласно Wildberries API
        Request request = new Request.Builder()
                .url(getUrl(answerers.get(chatId)))
                .addHeader("Authorization", "Bearer " + apiKey)
                .patch(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        //System.out.println(request);

        // отправка запроса и обработка ответа
        OkHttpClient client = new OkHttpClient();

        // поле, которое содержит код, соответсвующий успешной отправке запроса
        final int successCode = 200;

        try (Response response = client.newCall(request).execute()) {
            if (response.code() != successCode) {
                throw new RuntimeException();
            }
            outputMessage.setText("Ответ успешно отправлен! \uD83D\uDE4C");
            answerers.remove(chatId);
        } catch (Exception e) {
            outputMessage.setText("Возникла ошибка при отправке ответа, попробуйте еще раз \uD83D\uDCAA");
            throw new RuntimeException(e);
        }

        return outputMessage;
    }

    /**
     * Формирует тело запроса из ответа пользователя на отзыв/вопрос и ID отзыва/вопроса.
     * Тело запроса парсится в формат JSON.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @param answersMessage сообщение пользователя, которое он ввел в качестве ответа
     * @return тело запроса в формате JSON
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    private static String getJsonBody(String chatId, TypeOfOperations typeOfOperations, String answersMessage)
            throws JsonProcessingException {
        // получение ID отзыва/вопроса
        final String objectId = OneObject.getFirstObjectId(chatId, typeOfOperations);
        //System.out.println("getJsonBodyID = " + objectId);
        final ObjectMapper mapper = new ObjectMapper();

        if (typeOfOperations.equals(TypeOfOperations.FEEDBACKS)) {
            // ID отзыва и ответ пользователя передаются в объект answer
            final FeedbacksAnswer answer = new FeedbacksAnswer(objectId, answersMessage);
            // объект answer парсится в JSON
            return mapper.writeValueAsString(answer);
        }

        final Answer usersMessage = new Answer(answersMessage);
        // ID вопроса и ответ пользователя передаются в объект answer
        final QuestionsAnswer answer = new QuestionsAnswer(objectId, usersMessage);

        return mapper.writeValueAsString(answer);
    }

    /**
     * Формирует URL по которому будет осуществляться запрос к Wildberries для отправки ответа на отзыв/вопрос.
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @return URL для Wildberries API
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    private static String getUrl(TypeOfOperations typeOfOperations) {
        String request = getRequest(typeOfOperations, TypeOfApi.STANDART_API);

        HttpUrl.Builder urlBuilder =
                Objects.requireNonNull(HttpUrl.parse(getHalfUrl(TypeOfApi.STANDART_API)
                        + request)).newBuilder();

        return urlBuilder.build().toString();
    }

    /**
     * Метод для выбора переменной, которая будет обозначать вопрос/отзыв в сообщениях для пользователя.
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @return поле NAME_OF_QUESTIONS или NAME_OF_FEEDBACKS
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    private static String getNameOfObject(TypeOfOperations typeOfOperations) {
        if (typeOfOperations.equals(TypeOfOperations.QUESTIONS)) {
            return NAME_OF_QUESTIONS;
        }

        return NAME_OF_FEEDBACKS;
    }
}
