package wildberries.typesOfOperations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale {
    private String supplierArticle;
    private int forPay;
    private String brand;
    @JsonProperty("isStorno")
    private int isStorno;

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }
    public void setForPay(int forPay) {
        this.forPay = forPay;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setIsStorno(int isStorno) {
        this.isStorno = isStorno;
    }
    @Override
    public String toString() {

        String cancel = "нет";
        if (isStorno == 1) {
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
