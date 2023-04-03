package telegram.data;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.WBdata;

import static telegram.MyBot.setButtons;
import static telegram.MyBot.shop;

public class Statistics {

    public static SendMessage setStatisticsApi(Update update) {
        String inputMessage = update.getMessage().getText().trim();
        shop.setStatisticsApi(inputMessage);
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        SendMessage otputMessage = new SendMessage();
        otputMessage.setChatId(shop.getChatId());

        try {
            Parsing.dataToString(ordersToday);

            shop.setStatisticsApiMessage(false);
            otputMessage.setText("Отлично, можем приступать к работе! ✨");
            otputMessage.setReplyMarkup(setButtons());
        } catch (Exception e) {
            otputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли скопирован ключ "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return otputMessage;
    }

    public static SendMessage getTodayOrders() {
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());
        SendMessage outputMessage = new SendMessage();

        outputMessage.setChatId(shop.getChatId());
        outputMessage.setText(Parsing.dataToString(ordersToday));

        return outputMessage;
    }
}
