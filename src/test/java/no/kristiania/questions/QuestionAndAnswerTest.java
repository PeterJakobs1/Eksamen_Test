package no.kristiania.questions;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAndAnswerTest {

    private final QuestionAndAnswerDao dao = new QuestionAndAnswerDao(TestData.testDataSource());

    @Test
    void shouldSaveQuestionAndAnswer() throws SQLException {
        QuestionAndAnswer questionAndAnswer = exampleQuestionAndAnswer();
        dao.saveQuestionWithAnswer(questionAndAnswer);

        assertThat(dao.retrieveQuestionAndAnswerId(questionAndAnswer.getId()))
                .usingRecursiveComparison()
                .isEqualTo(questionAndAnswer)
        ;


    }

    public static QuestionAndAnswer exampleQuestionAndAnswer() {
        QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer();
        questionAndAnswer.setAnsweredQuestion(TestData.pickOne("Got Milk?","Like Food?"));
        questionAndAnswer.setQuestionAnswer(TestData.pickOne("Yes","No","Maybe"));
        return questionAndAnswer;
    }
}
