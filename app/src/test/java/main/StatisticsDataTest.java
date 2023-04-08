package main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wildberries.Parsing;
import wildberries.WBdata;

import static telegram.MyBot.SHOP;

public class StatisticsDataTest {

    @BeforeAll
    public static void setApi() {
        SHOP.setStatisticsApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6ImJmNDA1NTIwLTAxMTItNGViYi1iZ"
                + "TgyLTcyZWVlNmY4ZDU2NSJ9.oI8dyAntS5EHooBud_N-7qEPIO-e_jkPd_469WzgovI");
    }

    @Test
    public void orderTest() {
        final String ordersToday = WBdata.getDataForTheDay(SHOP.getStatisticsApi(), "order");
        final String result = Parsing.dataToString(ordersToday, "order");

        //System.out.println(result);
    }

}
