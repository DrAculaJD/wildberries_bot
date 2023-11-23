package wildberries.typeOfOperations.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typeOfOperations.DataFromWildberries;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс представляет собой структуру данных которые возвращаются при отправке запроса
 * к серверу Wildberries на получение продаж за текущий день.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale implements DataFromWildberries {
    private String supplierArticle;
    private String date;
    private String orderType;
    private String subject;
    private int forPay;
    private String brand;
    private String barcode;
    private int nmId;

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public void setNmId(int nmId) {
        this.nmId = nmId;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }
    public void setForPay(int forPay) {
        this.forPay = forPay;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * В этом классе в таком методе нет необходимости, поэтому он существует только потому,
     * что такова структура интерфейса DataFromWildberries.
     * @return null
     */
    @Override
    public String toString(String chatId) {
        return null;
    }

    /**
     * Метод форматирует одну запись о продаже.
     */
    @Override
    public String toString() {

        // расчет и округление суммы, которую поставщик получит за продажу
        double amount = forPay;
        BigDecimal bd = new BigDecimal(amount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        amount = bd.doubleValue();

        return "Дата продажи: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Тип товара: " + subject + '\n'
                + "Бренд товара: " + brand + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "К перечислению поставщику: " + amount + '\n'
                + "Тип заказа: " + orderType;
    }

}
