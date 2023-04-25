package telegram.data;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static main.Main.userSQL;

public class Statistics {

    public static SendMessage setStatisticsApi(Update update) {
        final String statisticsApiKey = update.getMessage().getText().trim();
        final String chatId = update.getMessage().getChatId().toString();

        final String ordersToday = WBdata.getDataForTheDay(statisticsApiKey, TypeOfOperations.ORDER);

        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);

        try {
            Parsing.dataToString(ordersToday, TypeOfOperations.ORDER);

            userSQL.setTelegramUser(chatId, statisticsApiKey);

            outputMessage.setText("Отлично, можем приступать к работе! ✨\nТеперь команды в меню работают.");
        } catch (Exception e) {
            outputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли он скопирован "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return outputMessage;
    }

    public static SendMessage getTodayData(String chatId, TypeOfOperations type) {
        final SendMessage outputMessage = new SendMessage();
        final String statisticsApi = userSQL.getStatisticsApi(chatId);

        final String ordersToday = WBdata.getDataForTheDay(statisticsApi, type);

        outputMessage.setChatId(chatId);
        outputMessage.setText(Parsing.dataToString(ordersToday, type));

        return outputMessage;
    }
}
