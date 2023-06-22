package wildberries.typeOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Класс представляет собой структуру объекта <b>product</b> который возвращается при отправке запроса
 * к серверу Wildberries на получение отзывов. В нем содержатся данные о товаре, к которому относится отзыв/вопрос.
 */
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
