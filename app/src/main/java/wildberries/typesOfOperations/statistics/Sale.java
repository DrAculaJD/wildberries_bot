package wildberries.typesOfOperations.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.DataFromWildberries;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale implements DataFromWildberries {
    private String supplierArticle;
    private String date;
    private String saleID;
    private String subject;
    private int forPay;
    private String brand;

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
    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    @Override
    public String toString(String chatId){
        return null;
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

        return "Время продажи: " + date.substring(0, 10) + " " + date.substring(11) + '\n'
                + "Тип товара: " + subject + '\n'
                + "Бренд товара: " + brand + '\n'
                + "Артикул продавца: " + supplierArticle + '\n'
                + "К перечислению поставщику: " + amount + '\n'
                + "Возврат: " + cancel;
    }

}
