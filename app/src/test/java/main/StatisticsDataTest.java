package main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wildberries.Parsing;
import wildberries.WBdata;
import wildberries.typesOfOperations.TypeOfOperations;

import static telegram.MyBot.SHOP;

public class StatisticsDataTest {

    @BeforeAll
    public static void setApi() {
        SHOP.setStatisticsApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6ImJmNDA1NTIwLTAxMTItNGViYi1iZ"
                + "TgyLTcyZWVlNmY4ZDU2NSJ9.oI8dyAntS5EHooBud_N-7qEPIO-e_jkPd_469WzgovI");
    }

    @Test
    public void ordersTest() {
        final String ordersToday = WBdata.getDataForTheDay(SHOP.getStatisticsApi(), TypeOfOperations.ORDER);
        final String result = Parsing.dataToString(ordersToday, TypeOfOperations.ORDER);

        System.out.println(ordersToday);
        System.out.println(result);
    }

    @Test
    public void salesTest() {
        final String salesToday = WBdata.getDataForTheDay(SHOP.getStatisticsApi(), TypeOfOperations.SALE);
        final String result = Parsing.dataToString(salesToday, TypeOfOperations.SALE);

        System.out.println(salesToday);
        System.out.println(result);
    }

}
