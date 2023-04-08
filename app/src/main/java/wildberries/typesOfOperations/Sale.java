package wildberries.typesOfOperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale implements StatisticsData {
    private String supplierArticle;
    private String date;
    private String lastChangeDate;
    private String saleID;
    private int forPay;
    private String brand;

    @Override
    public String getDate() {
        return date.substring(0, 10);
    }

    @Override
    public String getLastChangeOrderDate() {
        return lastChangeDate.substring(0, 10);
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
    public void setForPay(int forPay) {
        this.forPay = forPay;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    @Override
    public String toString() {

        String cancel = "нет \uD83D\uDC4D\n";
        if (saleID.contains("R")) {
            cancel = "да ❌";
        }

        double amount = forPay;
        BigDecimal bd = new BigDecimal(amount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        amount = bd.doubleValue();

        return "Время заказа: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Бренд товара: " + brand + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "К перечислению поставщику: " + amount + '\n'
                + "Возврат: " + cancel + '\n';
    }

}
