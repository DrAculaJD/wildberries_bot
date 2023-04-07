package telegram.data;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.WBdata;

import static telegram.MyBot.setButtons;
import static telegram.MyBot.SHOP;

public class Statistics {

    public static SendMessage setStatisticsApi(Update update) {
        String inputMessage = update.getMessage().getText().trim();
        SHOP.setStatisticsApi(inputMessage);
        final String ordersToday = WBdata.getDataForTheDay(SHOP.getStatisticsApi(), "order");

        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(SHOP.getChatId());

        try {
            Parsing.dataToString(ordersToday, "order");

            SHOP.setStatisticsApiMessage(false);
            outputMessage.setText("Отлично, можем приступать к работе! ✨");
            outputMessage.setReplyMarkup(setButtons());
        } catch (Exception e) {
            outputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли скопирован ключ "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return outputMessage;
    }

    public static SendMessage getTodayData(String typeOfData) {
        final SendMessage outputMessage = new SendMessage();
        final String ordersToday = WBdata.getDataForTheDay(SHOP.getStatisticsApi(), typeOfData);

        outputMessage.setChatId(SHOP.getChatId());
        outputMessage.setText(Parsing.dataToString(ordersToday, typeOfData));

        return outputMessage;
    }
}
