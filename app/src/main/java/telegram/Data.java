package telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static main.Main.userSQL;

public class Data {

    public static SendMessage setApiKey(Update update, TypeOfApi typeOfApi) {
        final String apiKey = update.getMessage().getText().trim();
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

    public static SendMessage getTodayData(String chatId, TypeOfOperations typeOfOperations, TypeOfApi typeOfApi) {
        final SendMessage outputMessage = new SendMessage();
        final String api = userSQL.getApi(chatId, typeOfApi);

        final String ordersToday = WBdata.getDataForTheDay(api, typeOfOperations, typeOfApi);

        outputMessage.setChatId(chatId);
        outputMessage.setText(Parsing.dataToString(chatId, ordersToday, typeOfOperations));

        return outputMessage;
    }

    public static boolean isStatisticsKey(String chatId, String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getDataForTheDay(apiKey, typeOfOperations, TypeOfApi.STATISTICS_API);
            Parsing.dataToString(chatId, dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

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
