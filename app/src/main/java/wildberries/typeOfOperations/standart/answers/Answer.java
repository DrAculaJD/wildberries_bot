package wildberries.typeOfOperations.standart.answers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Класс представляет объект <b>answer</b>, в котором хранится ответ
 * на вопрос клиента в поле <b>text</b>. Создан для парсинга ответа на вопрос в формат JSON,
 * который требуется для отправки запроса на сервер Wildberries.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {
    private String text;

    public Answer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
