package wildberries.typesOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.DataFromWildberries;
import wildberries.typesOfOperations.TypeOfOperations;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;
import wildberries.typesOfOperations.standart.objectStructure.QuestionData;

import static wildberries.typesOfOperations.standart.objectStructure.OneObject.setFirstObjectId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements DataFromWildberries {

    private QuestionData data;

    public void setData(QuestionData questionData) {
        this.data = questionData;
    }

    @Override
    public String toString() {
        return "Вопросов без ответа еще нет ⛔  ";
    }

    @Override
    public String toString(String chatId) {

        if (data.getCountUnanswered() == 0) {
            return "Вопросов без ответа еще нет ⛔  ";
        }

        StringBuilder result = new StringBuilder();
        int counter = 1;

        for (OneObject question: data.getQuestions()) {
            result.append("❓ Вопрос №").append(counter).append("\n");
            result.append(question.getCreatedDate());
            result.append(question.getProductDetails().toString());
            result.append(question.getText());
            setFirstObjectId(chatId, question.getId(), counter++, TypeOfOperations.QUESTIONS);
            //System.out.println(question.getId());
        }

        return result.toString();
    }
}
