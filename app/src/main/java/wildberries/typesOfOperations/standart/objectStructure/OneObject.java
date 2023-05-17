package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneObject {
    private String text;
    private String createdDate;
    private int productValuation;
    private Product productDetails;

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setProductDetails(Product productDetails) {
        this.productDetails = productDetails;
    }

    public void setProductValuation(int productValuation) {
        this.productValuation = productValuation;
    }

    public String getProductValuation() {
        final String star = "⭐";

        return "Оценка покупателя: " + star.repeat(productValuation) + '\n';
    }

    public String getText() {
        return "Содержание: " + text + '\n';
    }

    public String getCreatedDate() {
        return "Дата создания: " + createdDate.substring(0, 10) + '\n';
    }

    public Product getProductDetails() {
        return productDetails;
    }
}
