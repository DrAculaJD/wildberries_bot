package main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wildberries.Parsing;
import wildberries.WBdata;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    private final String STATISTICS_API = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6ImJmNDA1NTIwLTAxMTI"
            + "tNGViYi1iZTgyLTcyZWVlNmY4ZDU2NSJ9.oI8dyAntS5EHooBud_N-7qEPIO-e_jkPd_469WzgovI";

    @BeforeAll
    public static void setDate() {
        WBdata.setFormattedDate("2023-04-03");
    }

    @Test
    public void salesParsingTest() {
        final String salesToday = WBdata.getSalesForTheDay(STATISTICS_API);
        final String result = Parsing.salesToString(salesToday);

        final String salesResult = """
                Продажа №1
                Артикул продавца: 0020001
                Бренд товара: Acer
                К перечислению поставщику: 1029.0
                Возврат: нет

                Продажа №2
                Артикул продавца: 0020001
                Бренд товара: Acer
                К перечислению поставщику: -218.0
                Возврат: нет

                Продажа №3
                Артикул продавца: 0020044
                Бренд товара: lenovo
                К перечислению поставщику: -1253.0
                Возврат: да ❌

                Продажа №4
                Артикул продавца: 0020001
                Бренд товара: Acer
                К перечислению поставщику: 1029.0
                Возврат: нет

                Продажа №5
                Артикул продавца: 0020015
                Бренд товара: Asus
                К перечислению поставщику: 1005.0
                Возврат: нет""";

        assertThat(result).isEqualTo(salesResult);
    }

    @Test
    public void ordersParsingTest() {
        final String salesToday = WBdata.getOrdersForTheDay(STATISTICS_API);
        final String result = Parsing.ordersToString(salesToday);

        final String ordersResult = """
                Заказ №1
                Бренд: lenovo
                Артикул продавца: 0020044
                Стоимость: 1161.71 руб.
                Склад продажи: Электросталь
                Куда заказан: Омская
                Отменен: нет

                Заказ №2
                Бренд: Acer
                Артикул продавца: 0020001
                Стоимость: 1181.4 руб.
                Склад продажи: Электросталь
                Куда заказан: Тверская
                Отменен: нет

                Заказ №3
                Бренд: lenovo
                Артикул продавца: 0020044
                Стоимость: 1181.4 руб.
                Склад продажи: Краснодар 2
                Куда заказан:\s
                Отменен: нет

                Заказ №4
                Бренд: lenovo
                Артикул продавца: 0020044
                Стоимость: 1181.0 руб.
                Склад продажи: Электросталь
                Куда заказан: Пензенская
                Отменен: нет""";

        assertThat(result).isEqualTo(ordersResult);
    }

}
