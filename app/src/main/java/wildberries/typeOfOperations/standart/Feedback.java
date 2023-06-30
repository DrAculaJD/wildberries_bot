package wildberries.typeOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typeOfOperations.DataFromWildberries;
import wildberries.typeOfOperations.TypeOfOperations;
import wildberries.typeOfOperations.standart.objectStructure.FeedbackData;
import wildberries.typeOfOperations.standart.objectStructure.OneObject;

import static wildberries.typeOfOperations.standart.objectStructure.OneObject.setFirstObjectId;

/**
 * Класс представляет собой структуру данных которые возвращаются при отправке запроса
 * к серверу Wildberries на получение отзывов.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feedback implements DataFromWildberries {

    private FeedbackData data;

    public void setData(FeedbackData data) {
        this.data = data;
    }

    /**
     * Метод возвращает сообщение <b>"Отзывов без ответа еще нет ⛔  "</b>.
     */
    @Override
    public String toString() {
        return "Отзывов без ответа еще нет ⛔  ";
    }

    /**
     * Метод форматирует список отзывов без ответа, для отправки списка пользователю.
     * В случае, если отзывов без ответа нет, метод возвращает сообщение <b>"Отзывов без ответа еще нет ⛔  "</b>.
     * @param chatId ID Telegram чата пользователя
     */
    @Override
    public String toString(String chatId) {
        if (data.getCountUnanswered() == 0) {
            return "Отзывов без ответа еще нет ⛔  ";
        }

        StringBuilder result = new StringBuilder();
        int counter = 1;

        for (OneObject feedback: data.getFeedbacks()) {
            result.append("\uD83D\uDD14 Отзыв №").append(counter).append("\n");
            result.append(feedback.getCreatedDate());
            result.append(feedback.getProductDetails().toString());
            result.append(feedback.getProductValuation());
            result.append(feedback.getText()).append("\n");
            // ID первого из отзывов без ответа добавляется в Map OneObject#firstObject,
            // чтобы на него можно было отправить ответ
            setFirstObjectId(chatId, feedback.getId(), counter++, TypeOfOperations.FEEDBACKS);
        }

        return result.toString();
    }

}
