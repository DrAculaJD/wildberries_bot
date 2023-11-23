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
    private String regionName;
    private String brand;
    private String orderType;
    private String barcode;
    private int nmId;

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
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
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    public void setBrand(String brand) {
        this.brand = brand;
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

        // расчет и округление суммы, которую поставщик получит за заказ
        double priceWithDiscount = totalPrice * (1 - (double) discountPercent / 100);
        BigDecimal bd = new BigDecimal(priceWithDiscount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        priceWithDiscount = bd.doubleValue();

        return "Дата заказа: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Бренд: " + brand + '\n'
                + "Тип товара: " + subject + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "Стоимость: " + priceWithDiscount + " руб." + '\n'
                + "Склад продажи: " + warehouseName + '\n'
                + "Куда заказан: " + regionName + '\n'
                + "Тип заказа: " + getOrderType(orderType) + '\n';
    }

    /**
     * Форматирует сообщение с типом заказа, по сути добавляет соответсвующий заказу или возврату эмодзи,
     * чтобы визуально было легче различать их между собой.
     * @param type Тип заказа, котрый возвращает переменная orderType
     * @return Тип заказа с соответсвующим эмодзи
     */
    public static String getOrderType(String type) {
        final String clientOrder = "Клиентский";

        if (type.equals(clientOrder)) {
            return clientOrder + " 📦";
        }

        return type + " \uD83D\uDD03";
    }
}
