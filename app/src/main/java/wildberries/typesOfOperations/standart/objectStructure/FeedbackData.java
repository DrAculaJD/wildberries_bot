package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

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
