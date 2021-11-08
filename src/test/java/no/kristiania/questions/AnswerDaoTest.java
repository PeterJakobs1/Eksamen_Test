package no.kristiania.questions;

import no.kristiania.http.AnswerOptionsController;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpServer;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;


import static org.assertj.core.api.Assertions.assertThat;


public class AnswerDaoTest {
    private final HttpServer server = new HttpServer(0);

    private AnswerDao dao = new AnswerDao(TestData.testDataSource());

    public AnswerDaoTest() throws IOException {
    }

    @Test
    public void shouldListAnswers() throws SQLException, IOException {
        AnswerDao answerDao = new AnswerDao((TestData.testDataSource()));
        Answer firstAnswer = AnswerDaoTest.exampleAnswer();
        answerDao.saveAnswer(firstAnswer);
        Answer secondAnswer = AnswerDaoTest.exampleAnswer();
        answerDao.saveAnswer(secondAnswer);

        server.addController("/api/answerOptions",new AnswerOptionsController(answerDao));

        HttpClient client = new HttpClient("localhost", server.getPort(),"/api/answerOptions");
        assertThat(client.getMessageBody())
                .contains(firstAnswer.getAnswerName())
                .contains(secondAnswer.getAnswerName());
    }

    public static Answer exampleAnswer() {
        Answer answer = new Answer();
        answer.setAnswerName(TestData.pickOne("Fish?","Bread?","Hat?"));
        return answer;
    }

}
