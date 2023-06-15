package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionData {
    private List<OneObject> questions;
    private int countUnanswered;

    public int getCountUnanswered() {
        return countUnanswered;
    }

    public void setCountUnanswered(int countUnanswered) {
        this.countUnanswered = countUnanswered;
    }

    public void setQuestions(List<OneObject> questions) {
        this.questions = questions;
    }

    public List<OneObject> getQuestions() {
        return questions;
    }
}
