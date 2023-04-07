package wildberries.typesOfOperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale {
    private String supplierArticle;
    private String saleID;
    private int forPay;
    private String brand;

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

        return "Артикул продавца: " + supplierArticle + '\n'
                + "Бренд товара: " + brand + '\n'
                + "К перечислению поставщику: " + amount + '\n'
                + "Возврат: " + cancel + '\n';
    }
}
