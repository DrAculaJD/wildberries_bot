package wildberries.typesOfOperations.standart.answers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Класс представляет объект, структура которого соответсвует телу запроса, в котором отправляется ответ на отзыв.
 * Класс создан для парсинга тела запроса в формат JSON, который требуется для отправки на сервер Wildberries.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionsAnswer {

    private String id;
    private final String state = "wbRu";
    private Answer answer;

    public QuestionsAnswer(String id, Answer answer) {
        this.id = id;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public Answer getAnswer() {
        return answer;
    }
}
