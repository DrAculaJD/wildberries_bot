package wildberries.typesOfOperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements StatisticsData {
    private String supplierArticle;
    private String date;
    private String lastChangeDate;
    private double totalPrice;
    private int discountPercent;
    private String warehouseName;
    private String oblast;
    private String brand;
    @JsonProperty("isCancel")
    private boolean isCancel;

    @Override
    public String getLastChangeOrderDate() {
        return lastChangeDate.substring(0, 10);
    }

    public String getDate() {
        return date.substring(0, 10);
    }

    public void setLastChangeDate(String newLastChangeDate) {
        lastChangeDate = newLastChangeDate;
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

    @Override
    public String toString() {
        String cancel = "нет \uD83D\uDC4D\n";

        double priceWithDiscount = totalPrice * (1 - (double) discountPercent / 100);
        BigDecimal bd = new BigDecimal(priceWithDiscount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        priceWithDiscount = bd.doubleValue();

        if (isCancel) {
            cancel = "да ❌";
        }

        return "Бренд: " + brand + '\n'
                + "Время заказа: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "Стоимость: " + priceWithDiscount + " руб." + '\n'
                + "Склад продажи: " + warehouseName + '\n'
                + "Куда заказан: " + oblast + '\n'
                + "Отменен: " + cancel + '\n';
    }
}
