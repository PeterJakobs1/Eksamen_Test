package no.kristiania.questions;

public class QuestionAndAnswer {
    private Long id;
    private String answeredQuestion;
    private String questionAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnsweredQuestion() {
        return answeredQuestion;
    }

    public void setAnsweredQuestion(String answeredQuestion) {
        this.answeredQuestion = answeredQuestion;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    @Override
    public String toString() {
        return "QuestionAndAnswer{" +
                "id=" + id +
                ", answeredQuestion='" + answeredQuestion + '\'' +
                ", questionAnswer='" + questionAnswer + '\'' +
                '}';
    }
}
