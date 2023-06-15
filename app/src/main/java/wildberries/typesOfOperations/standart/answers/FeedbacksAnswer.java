package wildberries.typesOfOperations.standart.answers;

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
