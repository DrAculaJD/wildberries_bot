package main;

import database.UserSQL;
import org.junit.jupiter.api.Test;
import wildberries.Parsing;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static main.Main.userSQL;

public class StatisticsDataTest {
    private final String chatId = "796246822";
    private final String statApi = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6ImJmNDA1NTIwLTAxMTItNGViYi1iZ"
            + "TgyLTcyZWVlNmY4ZDU2NSJ9.oI8dyAntS5EHooBud_N-7qEPIO-e_jkPd_469WzgovI";

    @Test
    public void ordersTest() {
        final String ordersToday = WBdata.getDataForTheDay(statApi, TypeOfOperations.ORDER);
        final String result = Parsing.dataToString(ordersToday, TypeOfOperations.ORDER);

        System.out.println(ordersToday);
        System.out.println(result);
    }

    @Test
    public void salesTest() {
        final String salesToday = WBdata.getDataForTheDay(statApi, TypeOfOperations.SALE);
        final String result = Parsing.dataToString(salesToday, TypeOfOperations.SALE);

        System.out.println(salesToday);
        System.out.println(result);
    }

    @Test
    public void setUserToSqlTest() {
        userSQL = new UserSQL("wbBot", "542300", "jdbc:postgresql://localhost:5432/wbBot");
        userSQL.setTelegramUser(chatId, statApi);

        userSQL.baseContainsId(chatId);

    }

}
