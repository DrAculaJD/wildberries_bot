package telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static main.Main.userSQL;

/**
 * Класс служит прослойкой между методами, которые выполняют обработку данных,
 * поступающих из Telegram бота и методами, которые взаимодействуют с другими сервисами (Wildberries, SQL).
 * Содержит методы, которые обращаются к серверу Wildberries и базе данных, форматируют и передают эти данные
 * в методы класса MyBot.
 */

public class Data {

    /**
     * Получает сообщение из бота, которое должно быть API ключем пользователя, и передает в класс <b>userSQL</b>
     * для записи в базу данных.
     * @param update сообщение пользователя из Telegram
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return объект типа <b>SendMessage</b>, в котором содержится сообщение
     * об успешном/неуспешном добавлении API ключа в БД
     * @see org.telegram.telegrambots.meta.api.objects.Update
     * @see wildberries.TypeOfApi
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage setApiKey(Update update, TypeOfApi typeOfApi) {
        // получение текста сообщения
        final String apiKey = update.getMessage().getText().trim();
        // получение ID Telegram чата
        final String chatId = update.getMessage().getChatId().toString();

        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        try {
            userSQL.setTelegramUser(chatId, apiKey, typeOfApi);

            outputMessage.setText("Отлично, ключ работает! ✨");
        } catch (Exception e) {
            outputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли он скопирован "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return outputMessage;
    }

    /**
     * Метод получает данные от сервера Wildberries и форматирует их для отправки пользователю.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод
     * @param typeOfApi тип API ключа, который требуется добавить в БД
     * @return объект <b>SendMessage</b>, который содержит сообщение для пользователя
     * @see wildberries.TypeOfApi
     * @see wildberries.typesOfOperations.TypeOfOperations
     * @see org.telegram.telegrambots.meta.api.methods.send.SendMessage
     */
    public static SendMessage getTodayData(String chatId, TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {
        final SendMessage outputMessage = new SendMessage();
        final String api = userSQL.getApi(chatId, typeOfApi);

        final String ordersToday = WBdata.getDataForTheDay(api, typeOfOperations, typeOfApi);

        outputMessage.setChatId(chatId);
        outputMessage.setText(Parsing.dataToString(chatId, ordersToday, typeOfOperations));

        return outputMessage;
    }

    /**
     * Метод проверяет, соответсвует ли тип переданного API ключа типу "Статистика"
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который ввел пользователь
     * @param typeOfOperations тип объекта, с которым работает метод
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    public static boolean isStatisticsKey(String chatId, String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getDataForTheDay(apiKey, typeOfOperations, TypeOfApi.STATISTICS_API);
            Parsing.dataToString(chatId, dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    /**
     * Метод проверяет, соответсвует ли тип переданного API ключа типу "Стандартный"
     * @param chatId ID Telegram чата пользователя
     * @param apiKey API ключ, который ввел пользователь
     * @param typeOfOperations тип объекта, с которым работает метод
     * @see wildberries.typesOfOperations.TypeOfOperations
     */
    public static boolean isStandartKey(String chatId, String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getDataForTheDay(apiKey, typeOfOperations, TypeOfApi.STANDART_API);
            Parsing.dataToString(chatId, dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }
}
