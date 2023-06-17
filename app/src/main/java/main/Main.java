package main;

import database.UserSQL;
import telegram.MyBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static UserSQL userSQL;

    /**
     * Основной класс, создает объект класса UserSQL для работы с базой данных, и создает подключение к Telegram боту.
     * @param args принимает 5 значений в качестве аргументов при запуске программы:<br>
     *             - имя пользователя Telegram бота<br>
     *             - токен Telegram бота<br>
     *             - имя пользователя базы PostgreSQL<br>
     *             - пароль пользователя PostgreSQL<br>
     *             - путь к базе PostgreSQL
     */
    public static void main(String[] args) {
        final String username = args[0];
        final String token = args[1];
        final String sqlUsername = args[2];
        final String sqlPassword = args[3];
        final String sqlPath = args[4];
        // объект userSQL создается для получения доступа к базе данных
        userSQL = new UserSQL(sqlUsername, sqlPassword, sqlPath);

        // код ниже нужен для подключения к телеграм-боту
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
