package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.TypeOfOperations;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneObject {
    // Map для храрнения id отзывов и вопросов, на которые можно написать ответ
    public static Map<String, Map<TypeOfOperations, String>> firstObject = new HashMap<>();
    private String text;
    private String id;
    private String createdDate;
    private int productValuation;
    private Product productDetails;

    // метод для добавления id первого отзыва или вопроса из списка в OneObject.firstObject
    public static void setFirstObjectId(String chatId, String feedbackId, int counter,
                                        TypeOfOperations typeOfOperations) {
        Map<TypeOfOperations, String> firstFeedback = new HashMap<>();

        if (counter == 1) {
            firstFeedback.put(typeOfOperations, feedbackId);
            firstObject.put(chatId, firstFeedback);
        }
    }

    public static String getFirstObjectId (String chatId, TypeOfOperations typeOfOperations) {

            return firstObject.get(chatId).get(typeOfOperations);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
