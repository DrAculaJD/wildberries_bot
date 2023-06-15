package main;

import database.UserSQL;
import org.junit.jupiter.api.Test;
import wildberries.Parsing;
import wildberries.TypeOfApi;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static main.Main.userSQL;

public class DataFromWildberriesTest {
    private final String chatId = "796246822";
    private final String statisticsApi = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6ImJmNDA1NTIwLTAxMTItNG"
            + "ViYi1iZTgyLTcyZWVlNmY4ZDU2NSJ9.oI8dyAntS5EHooBud_N-7qEPIO-e_jkPd_469WzgovI";
    private final String standartApi = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6IjdjM2FmMmRmLWU4OGIt"
            + "NGQ0Zi1hODg2LWVlNTI4OTU1YjI4NyJ9._HJaxIkJCbgDihkutzY1JUYFjapmMIVAWOjBqp6jw1Q";
    private final String sqlUser = "wbBot";
    private final String sqlUserPassword = "542300";
    private final String sqlTableAdress = "jdbc:postgresql://localhost:5432/wbBot";


    @Test
    public void ordersTest() {
        final String ordersToday = WBdata.getDataForTheDay(statisticsApi, TypeOfOperations.ORDER,
                TypeOfApi.STATISTICS_API);
        final String result = Parsing.dataToString(chatId, ordersToday, TypeOfOperations.ORDER);

        System.out.println(ordersToday);
        System.out.println(result);
    }

    @Test
    public void salesTest() {
        final String salesToday = WBdata.getDataForTheDay(statisticsApi, TypeOfOperations.SALE,
                TypeOfApi.STATISTICS_API);
        System.out.println(salesToday);

        final String result = Parsing.dataToString(chatId, salesToday, TypeOfOperations.SALE);
        System.out.println(result);
    }

    @Test
    public void questionsTest() {
        final String salesToday = WBdata.getDataForTheDay(standartApi, TypeOfOperations.QUESTIONS,
                TypeOfApi.STANDART_API);
        System.out.println(salesToday);

        final String result = Parsing.dataToString(chatId, salesToday, TypeOfOperations.QUESTIONS);
        System.out.println(result);
    }

    @Test
    public void feedbacksTest() {
        final String salesToday = WBdata.getDataForTheDay(standartApi, TypeOfOperations.FEEDBACKS,
                TypeOfApi.STANDART_API);
        System.out.println(salesToday);

        final String result = Parsing.dataToString(chatId, salesToday, TypeOfOperations.FEEDBACKS);
        System.out.println(result);
    }

    @Test
    public void setUserToSqlTest() {
        userSQL = new UserSQL(sqlUser, sqlUserPassword, sqlTableAdress);
        userSQL.setTelegramUser(chatId, statisticsApi, TypeOfApi.STATISTICS_API);
        userSQL.setTelegramUser(chatId, standartApi, TypeOfApi.STANDART_API);
    }

}
