package telegram.data;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wildberries.Parsing;
import wildberries.WBdata;

import static telegram.MyBot.setButtons;
import static telegram.MyBot.shop;

public class Statistics {

    private static final SendMessage OUTPUT_MESSAGE = new SendMessage();

    public static SendMessage setStatisticsApi(Update update) {
        String inputMessage = update.getMessage().getText().trim();
        shop.setStatisticsApi(inputMessage);
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(shop.getChatId());

        try {
            Parsing.ordersToString(ordersToday);

            shop.setStatisticsApiMessage(false);
            outputMessage.setText("Отлично, можем приступать к работе! ✨");
            outputMessage.setReplyMarkup(setButtons());
        } catch (Exception e) {
            outputMessage.setText("К сожалению, этот ключ не работает, проверьте, правильно ли скопирован ключ "
                    + "и попробуйте еще раз \uD83D\uDE80");
        }

        return outputMessage;
    }

    public static SendMessage getTodayOrders() {
        final String ordersToday = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        OUTPUT_MESSAGE.setChatId(shop.getChatId());
        OUTPUT_MESSAGE.setText(Parsing.ordersToString(ordersToday));

        return OUTPUT_MESSAGE;
    }

    public static SendMessage getTodaySales() {
        final String salesToday = WBdata.getSalesForTheDay(shop.getStatisticsApi());

        OUTPUT_MESSAGE.setChatId(shop.getChatId());
        OUTPUT_MESSAGE.setText(Parsing.salesToString(salesToday));

        return OUTPUT_MESSAGE;
    }
}
