package wildberries.typesOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionData {
    private List<OneObject> questions;

    public void setQuestions(List<OneObject> questions) {
        this.questions = questions;
    }

    public List<OneObject> getQuestions() {
        return questions;
    }
}
