package wildberries.typesOfOperations.standart.answers;

/**
 * Класс представляет объект, в котором хранится ответ
 * на отзыв клиента в поле <b>text</b>. В поле <b>id</b> хранится ID отзыва, на который предоставляется ответ.
 * Класс для парсинга ответа на отзыв в формат JSON, который требуется для отправки запроса на сервер Wildberries.
 */
public class FeedbacksAnswer {
    private final String id;
    private final String text;

    public FeedbacksAnswer(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
