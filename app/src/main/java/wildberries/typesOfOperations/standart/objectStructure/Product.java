package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String productName;
    private String supplierArticle;
    private String brandName;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSupplierArticle(String supplierArticle) {
        this.supplierArticle = supplierArticle;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "Бренд товара: " + brandName + '\n'
                + "Название товара: " + productName + '\n'
                + "Артикул поставщика: " + supplierArticle + '\n';
    }
}
