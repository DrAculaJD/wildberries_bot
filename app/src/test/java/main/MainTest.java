package main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

        final String salesResult = "Продажа №1\n" +
                "Артикул продавца: 0020001\n" +
                "Бренд товара: Acer\n" +
                "К перечислению поставщику: 1029.0\n" +
                "Возврат: нет\n" +
                "\n" +
                "Продажа №2\n" +
                "Артикул продавца: 0020001\n" +
                "Бренд товара: Acer\n" +
                "К перечислению поставщику: -218.0\n" +
                "Возврат: нет\n" +
                "\n" +
                "Продажа №3\n" +
                "Артикул продавца: 0020044\n" +
                "Бренд товара: lenovo\n" +
                "К перечислению поставщику: -1253.0\n" +
                "Возврат: да ❌\n" +
                "\n" +
                "Продажа №4\n" +
                "Артикул продавца: 0020001\n" +
                "Бренд товара: Acer\n" +
                "К перечислению поставщику: 1029.0\n" +
                "Возврат: нет\n" +
                "\n" +
                "Продажа №5\n" +
                "Артикул продавца: 0020015\n" +
                "Бренд товара: Asus\n" +
                "К перечислению поставщику: 1005.0\n" +
                "Возврат: нет";

        assertThat(result).isEqualTo(salesResult);
    }

    @Test
    public void ordersParsingTest() {
        final String salesToday = WBdata.getOrdersForTheDay(STATISTICS_API);
        final String result = Parsing.ordersToString(salesToday);

        final String ordersResult = "Заказ №1\n" +
                "Бренд: lenovo\n" +
                "Артикул продавца: 0020044\n" +
                "Стоимость: 1161.71 руб.\n" +
                "Склад продажи: Электросталь\n" +
                "Куда заказан: Омская\n" +
                "Отменен: нет\n" +
                "\n" +
                "Заказ №2\n" +
                "Бренд: Acer\n" +
                "Артикул продавца: 0020001\n" +
                "Стоимость: 1181.4 руб.\n" +
                "Склад продажи: Электросталь\n" +
                "Куда заказан: Тверская\n" +
                "Отменен: нет\n" +
                "\n" +
                "Заказ №3\n" +
                "Бренд: lenovo\n" +
                "Артикул продавца: 0020044\n" +
                "Стоимость: 1181.4 руб.\n" +
                "Склад продажи: Краснодар 2\n" +
                "Куда заказан: \n" +
                "Отменен: нет\n" +
                "\n" +
                "Заказ №4\n" +
                "Бренд: lenovo\n" +
                "Артикул продавца: 0020044\n" +
                "Стоимость: 1181.0 руб.\n" +
                "Склад продажи: Электросталь\n" +
                "Куда заказан: Пензенская\n" +
                "Отменен: нет";

        assertThat(result).isEqualTo(ordersResult);
    }

}
