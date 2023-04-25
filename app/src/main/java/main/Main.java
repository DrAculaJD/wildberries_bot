package main;

import database.UserSQL;
import telegram.MyBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static UserSQL userSQL;

    public static void main(String[] args) {
        final String username = args[0];
        final String token = args[1];
        final String sqlUsername = args[2];
        final String sqlPassword = args[3];
        final String sqlPath = args[4];

        userSQL = new UserSQL(sqlUsername, sqlPassword, sqlPath);

        TelegramBotsApi telegramBotsApi;

        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            MyBot bot = new MyBot(username, token);
            telegramBotsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
