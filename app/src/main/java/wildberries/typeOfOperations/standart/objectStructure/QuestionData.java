package wildberries.typeOfOperations.standart.objectStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Класс представляет собой структуру объекта <b>data</b> который возвращается при отправке запроса
 * к серверу Wildberries на получение вопросов.
 */
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
