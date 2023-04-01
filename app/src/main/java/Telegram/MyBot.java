package Telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import wildberries.Parsing;
import wildberries.Shop;
import wildberries.WBdata;


public class MyBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "polezhaevTestBot";
    }

    @Override
    public String getBotToken() {
        return "5699977622:AAFVfx9eVU6_-1lHGD1DB0VGCbbjtDc1878";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String inputMessage = update.getMessage().getText();

        Shop shop = new Shop();
        shop.setStatisticsApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6I"
                + "jQ0YTNiZGY2LThkMzItNDg0Zi1iODA0LTQxNzY2M2IwYWFmZSJ9.5WG20bkJyl7D80DsqICp8n29b5GgD-IR7wXJN18kvzk");

        final String ordersData = WBdata.getOrdersForTheDay(shop.getStatisticsApi());

        if (update.getMessage() != null && inputMessage.equals("/start")) {
            SendMessage otputMessage = new SendMessage();
            otputMessage.setChatId(update.getMessage().getChatId().toString());
            otputMessage.setText(Parsing.dataToString(ordersData));

            try {
                execute(otputMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
