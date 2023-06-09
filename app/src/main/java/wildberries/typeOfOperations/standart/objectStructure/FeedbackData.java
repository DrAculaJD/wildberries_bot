package wildberries.typeOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Класс представляет собой структуру объекта <b>data</b> который возвращается при отправке запроса
 * к серверу Wildberries на получение отзывов.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackData {
    private List<OneObject> feedbacks;
    private int countUnanswered;

    public void setFeedbacks(List<OneObject> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void setCountUnanswered(int countUnanswered) {
        this.countUnanswered = countUnanswered;
    }

    public int getCountUnanswered() {
        return countUnanswered;
    }

    public List<OneObject> getFeedbacks() {
        return feedbacks;
    }
}
