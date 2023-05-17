package wildberries.typesOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typesOfOperations.DataFromWildberries;
import wildberries.typesOfOperations.standart.objectStructure.OneObject;
import wildberries.typesOfOperations.standart.objectStructure.QuestionData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements DataFromWildberries {

    private QuestionData data;

    public void setData(QuestionData questionData) {
        this.data = questionData;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int counter = 1;

        for (OneObject question: data.getQuestions()) {
            result.append("Вопрос №").append(counter).append("\n");
            result.append(question.getCreatedDate());
            result.append(question.getProductDetails().toString());
            result.append(question.getText());
            counter++;
        }

        if (result.isEmpty()) {
            return "Данных за сегодня еще нет \uD83D\uDCB9 ";
        }

        return result.toString();
    }
}
