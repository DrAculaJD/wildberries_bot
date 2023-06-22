package wildberries.typeOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typeOfOperations.TypeOfOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс представляет собой структуру данных которые возвращаются при отправке запроса
 * к серверу Wildberries на получение отзывов/вопросов.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneObject {
    /** Map для храрнения ID отзывов и вопросов, на которые можно написать ответ. */
    public static Map<String, Map<TypeOfOperations, String>> firstObject = new HashMap<>();
    private String text;
    private String id;
    private String createdDate;
    private int productValuation;
    private Product productDetails;

    /**
     * Метод для добавления ID первого из списка доступных для овета отзывов/вопросов в <b>OneObject.firstObject</b>.
     * @param chatId ID Telegram чата пользователя
     * @param feedbackId ID отзыва/вопроса
     * @param counter значение счетчива вопросов/отзывов без ответа
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @see wildberries.typeOfOperations.TypeOfOperations
     */
    public static void setFirstObjectId(String chatId, String feedbackId, int counter,
                                        TypeOfOperations typeOfOperations) {
        Map<TypeOfOperations, String> firstFeedback = new HashMap<>();
        if (counter == 1) {
            firstFeedback.put(typeOfOperations, feedbackId);
            //System.out.println(firstFeedback.get(TypeOfOperations.QUESTIONS));
            //System.out.println(firstFeedback.get(TypeOfOperations.FEEDBACKS));
            firstObject.put(chatId, firstFeedback);
        }
    }

    /**
     * Метод для получения ID вопроса/отзыва из объекта <b>OneObject.firstObject</b>.
     * @param chatId ID Telegram чата пользователя
     * @param typeOfOperations тип объекта, с которым работает метод (вопрос или ответ)
     * @return ID вопроса/отзыва из объекта <b>OneObject.firstObject</b>
     * @see OneObject#firstObject
     */
    public static String getFirstObjectId(String chatId, TypeOfOperations typeOfOperations) {

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
