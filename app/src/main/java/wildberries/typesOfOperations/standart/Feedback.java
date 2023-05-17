package wildberries.typesOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.DataFromWildberries;
import wildberries.typesOfOperations.standart.objectStructure.FeedbackData;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feedback implements DataFromWildberries {

    private FeedbackData data;

    public void setData(FeedbackData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (data.getCountUnanswered() == 0) {
            return "Данных за сегодня еще нет \uD83D\uDCB9 ";
        }

        StringBuilder result = new StringBuilder();
        int counter = 1;

        for (OneObject feedback: data.getFeedbacks()) {
            result.append("Отзыв №").append(counter).append("\n");
            result.append(feedback.getCreatedDate());
            result.append(feedback.getProductDetails().toString());
            result.append(feedback.getProductValuation());
            result.append(feedback.getText());
            counter++;
        }

        if (result.isEmpty()) {
            return "Данных за сегодня еще нет \uD83D\uDCB9 ";
        }

        return result.toString();
    }

}
