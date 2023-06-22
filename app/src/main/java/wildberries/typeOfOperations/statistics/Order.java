package wildberries.typeOfOperations.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import wildberries.typeOfOperations.DataFromWildberries;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс представляет собой структуру данных которые возвращаются при отправке запроса
 * к серверу Wildberries на получение заказов за текущий день.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements DataFromWildberries {
    private String supplierArticle;
    private String date;
    private double totalPrice;
    private String subject;
    private int discountPercent;
    private String warehouseName;
    private String oblast;
    private String brand;
    private String barcode;
    private int nmId;
    @JsonProperty("isCancel")
    private boolean isCancel;

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
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public void setOblast(String oblast) {
        this.oblast = oblast;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
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
     * Метод форматирует одну запись о заказе.
     */
    @Override
    public String toString() {
        // переменная хранит данные о том был ли отменен заказ и далее меняет свое значение
        // в зависимости от значения переменной isCancel
        String cancel = "нет \uD83D\uDC4D\n";

        // расчет и округление суммы, которую поставщик получит за заказ
        double priceWithDiscount = totalPrice * (1 - (double) discountPercent / 100);
        BigDecimal bd = new BigDecimal(priceWithDiscount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        priceWithDiscount = bd.doubleValue();

        if (isCancel) {
            cancel = "да ❌";
        }

        return "Дата заказа: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Бренд: " + brand + '\n'
                + "Тип товара: " + subject + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "Стоимость: " + priceWithDiscount + " руб." + '\n'
                + "Склад продажи: " + warehouseName + '\n'
                + "Куда заказан: " + oblast + '\n'
                + "Отменен: " + cancel;
    }
}
