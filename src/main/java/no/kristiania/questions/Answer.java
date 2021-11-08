package no.kristiania.questions;

public class Answer {
    private String answerName;
    private Long answerId;

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    @Override
    public String toString() {
        return "{" +
                "Alternativ " + answerId +
                ": " + answerName +
                '}';
    }
}
