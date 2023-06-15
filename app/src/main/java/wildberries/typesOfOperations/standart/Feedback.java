package wildberries.typesOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.DataFromWildberries;
import wildberries.typesOfOperations.TypeOfOperations;
import wildberries.typesOfOperations.standart.objectStructure.FeedbackData;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;

import static wildberries.typesOfOperations.standart.objectStructure.OneObject.setFirstObjectId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feedback implements DataFromWildberries {

    private FeedbackData data;

    public void setData(FeedbackData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Отзывов без ответа еще нет ⛔  ";
    }

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
            result.append(feedback.getText());
            setFirstObjectId(chatId, feedback.getId(), counter++, TypeOfOperations.FEEDBACKS);
        }

        return result.toString();
    }

}
