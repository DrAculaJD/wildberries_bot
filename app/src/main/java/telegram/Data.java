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
        final String statisticsApi = userSQL.getStatisticsApi(chatId);
        final String standartApi = userSQL.getStandartApi(chatId);
        String currentApi;

        if (typeOfApi.equals(TypeOfApi.STANDART_API)) {
            currentApi = standartApi;
        } else {
            currentApi = statisticsApi;
        }

        final String ordersToday = WBdata.getDataForTheDay(currentApi, typeOfOperations, typeOfApi);

        outputMessage.setChatId(chatId);
        outputMessage.setText(Parsing.dataToString(ordersToday, typeOfOperations));

        return outputMessage;
    }

    public static boolean isStatisticsKey(String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getDataForTheDay(apiKey, typeOfOperations, TypeOfApi.STATISTICS_API);
            Parsing.dataToString(dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public static boolean isStandartKey(String apiKey, TypeOfOperations typeOfOperations) {

        try {
            final String dataToday = WBdata.getDataForTheDay(apiKey, typeOfOperations, TypeOfApi.STANDART_API);
            Parsing.dataToString(dataToday, typeOfOperations);
        } catch (Exception e) {
            return false;
        }

        return true;

    }
}
