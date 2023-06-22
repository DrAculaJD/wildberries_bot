package wildberries.typeOfOperations.standart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wildberries.typeOfOperations.DataFromWildberries;
import wildberries.typeOfOperations.TypeOfOperations;
import wildberries.typeOfOperations.standart.objectStructure.OneObject;
import wildberries.typeOfOperations.standart.objectStructure.QuestionData;

import static wildberries.typeOfOperations.standart.objectStructure.OneObject.setFirstObjectId;

/**
 * Класс представляет собой структуру данных которые возвращаются при отправке запроса
 * к серверу Wildberries на получение вопросов.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements DataFromWildberries {

    private QuestionData data;

    public void setData(QuestionData questionData) {
        this.data = questionData;
    }

    /**
     * Метод возвращает сообщение <b>"Вопросов без ответа еще нет ⛔  "</b>.
     */
    @Override
    public String toString() {
        return "Вопросов без ответа еще нет ⛔  ";
    }

    /**
     * Метод форматирует список вопросов без ответа, для отправки списка пользователю.
     * В случае, если вопросов без ответа нет, метод возвращает сообщение <b>"Вопросов без ответа еще нет ⛔  "</b>.
     * @param chatId ID Telegram чата пользователя
     */
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
            // ID первого из вопросов без ответа добавляется в Map OneObject#firstObject,
            // чтобы на него можно было отправить ответ
            setFirstObjectId(chatId, question.getId(), counter++, TypeOfOperations.QUESTIONS);
            //System.out.println(question.getId());
        }

        return result.toString();
    }
}
