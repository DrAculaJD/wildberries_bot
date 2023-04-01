package main;

import Telegram.MyBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import wildberries.Parsing;
import wildberries.Shop;
import wildberries.WBdata;

import java.util.EmptyStackException;

public class Main {
    public static void main(String[] args) {
//        Shop shop = new Shop();
//        shop.setStatisticsApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6I"
//                + "jQ0YTNiZGY2LThkMzItNDg0Zi1iODA0LTQxNzY2M2IwYWFmZSJ9.5WG20bkJyl7D80DsqICp8n29b5GgD-IR7wXJN18kvzk");
//
//        final String ordersData = WBdata.getOrdersForTheDay(shop.getStatisticsApi());
//
//       System.out.println(Parsing.dataToString(ordersData));

        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try{
            telegramBotsApi.registerBot(new MyBot());
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

}
